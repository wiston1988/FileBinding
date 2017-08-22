# file-binding

1	 序言
1.1	背景
目前项目中常用的文件解析和错误处理能力比较薄弱，对于多种格式的文件处理需要没有很好的复用功能，代码比较繁琐，鉴于此，需要开发一个文件绑定功能和解析功能的中间件。
2	设计方案
2.1	概述
下图为文件解析绑定中间件工作流程：
 


2.2	IO 适配器
IO适配器就是描述怎样对每个类型文件读取或写入时进行行级别或单元格级别分割等。所有通用的文件读写IO都实现以下2个类：DocumentReader and DocumentWriter













工具类提供了以下通用的文件读写IO模板，用户也可自定义自己的IO适配器并实现上面给出的实现类。
CSV读写IO：
com.filebinding.core.io.csv.CSVReader
com.filebinding.core.io.csv.CSVWriter
03版Excel读写IO：
com.filebinding.core.io.excel.XLSReader
com.filebinding.core.io.excel.XLSWriter
07版Excel读写IO：
com.filebinding.core.io.excel.XLSXReader
com.filebinding.core.io.excel.XLSXWriter
普通文本读写IO：
com.filebinding.core.io.jdkAdapter.DocumentBufferReader
com.filebinding.core.io.jdkAdapter.DocumentBufferWriter

2.3	文件绑定策略
文件绑定策略用于解析和渲染文件的核心组件。
下面介绍工具已经实现的文件绑定策略类关系图，开发自己实现的可参考并继承对应的类：
 FileBindStrategy是所有最基本实现接口。下面介绍了5个类的使用场景：

	导出文件策略：
DefaultBuildingStrategy：能够满足大多数build，包括csv，excel，文本格式等。用户也可继承该类，重写所提供的方法来实现自己的策略。请参考测试类filebinding.BuildMain。

	导入文件策略：
DefaultParsingStrategy：满足基本的文件解析工作，读取文件中的每一行，且根据import configuration配置解析成对应的POJO。由于文件第一行不是header，所以文件每一列的位置需要和配置文件中的列对应，不可互换。请参考测试类filebinding.ParseMain：
			//测试没有header的文件解析
			new ParseMain().testParseNoHeaderFile(docConfigFactory);
HeaderMappingParsingStrategy：文件第一行需要包含头，import configuration配置文件中需要配置头的名字和POJO域对应关系，所以文件每一列位置可以任意互换。请参考测试类filebinding.ParseMain：
			//测试包含header的文件解析
			new ParseMain().testParseCSV(docConfigFactory);
			new ParseMain().testParseXLSX(docConfigFactory);
			new ParseMain().testParseXLS(docConfigFactory);
			new ParseMain().testAdvancedParser(docConfigFactory);
SubstrParsingStrategy：根据起始位置和结束位置来读取列，在配置文件import configuration中需要配置文件列的起始位置和结束位置以及POJO域的对于关系。文件每一列不可互换。

SubstrByLenthParsingStrategy：根据长度来读取列，位置从0开始递增，在配置文件import configuration中需要配置文件列读取固定长度和POJO域的对于关系。所以文件每一列位置不可互换。

2.4	XML 映射文件配置详解
配置文件包含export和build两类文件，在程序启动加载或web启动加载。两类配置文件的基本结构如下：




















	根节点配置
property	自定义全局属性，可以在自定义类中使用
auto-fieldParser
auto-fieldRenderer	自定义类型转换类的配置，如果记录配置中的列没有配置解析器或渲染器，那么会使用这个配置来解析或者渲染，依次根据配置顺序和方法boolean canConvert(DocumentFieldConfiguration config)返回值来判断用哪个类来解析或渲染。
classConfig	类的别名配置，类似ibatis中的typeAlias

	记录级别配置
tag	必填字段。记录解析或渲染的ID，不能重名。 
class	必填字段。对应的POJO类，该类可在classConfig中的别名，也可以是“包+java bean”。
parser/builder	必填字段。Parsing 或Building 策略名。
ignoreerror	如果配置true，文件解析某一行时报错，可忽略该行。默认为false。
dateFormat	为该配置下所有记录日期类型字段设置默认解析或渲染的格式，这个格式不统一，可以为任意表达式，只要自定义的解析器或渲染器能够识别就可以，如yyyyMMdd，则对应的日期解析器需要能识别该表达式并解析。可以详见解析例子配置[AdvancedImport]。
numberFormat	为该配置下所有记录数字类型字段设置默认解析或渲染的格式，这个格式不统一，可以为任意表达式，只要自定义的解析器或渲染器能够识别就可以，如numberFormat="5"可以表示为数字的长度，可以详见解析例子配置[AdvancedImport]。
labelSupport	该字段为导出功能使用，如果配置为true，导出时文件内容第一行有header，默认为false。

	POJO域级别配置
name	必填字段。必须和POJO中的属性名一致。
bind-to-column	对应的文件列的配置，比如，当策略为HeaderMappingParsingStrategy，就表示header，当策略为SubstrParsingStrategy，就表示起始位置和结束位置。
parser/renderer	自定义域解析器和渲染器
groupName	多个不同配置可以表示相同的域的时候可以使用，具体配置详见例子配置[TestImport]。
parseFormatStr 	域解析的格式配置。和parser配合使用。
buildFormatStr	域渲染的格式配置。和renderer配合使用。
formatStr	渲染或解析的格式配置。当parseFormatStr和buildFormatStr没有配置时，系统才会读取和使用该格式配置。
required	是否为空
valid	当配置为false，该列在解析或渲染将被忽略。默认为true。
	
3	使用例子 – 解析一个CSV文件到java bean
src/test文件夹下包含各个不同类型的文件解析和渲染例子，以及各个渲染器和解析器的写法。下面给出CSV文件解析步骤：
3.1	步骤1 – 映射文件配置









	
注：具体配置信息详解可参考前面章节。其他配置详见代码文件filebinding.config.ImportDocumentConfig.xml
3.2	步骤2 – IO适配器
可使用工具实现的com.filebinding.core.io.csv.CSVReader来初始化。
		CSVReader reader = new CSVReader(new FileReader(“FilePath”));

3.3	步骤3 – 解析策略
例子中采用自定义的ImportStrategy， 也可采用和继承工具中已经实现的策略，详细查看前面章节。

3.4	步骤4 – 编写主流程代码
详细参考代码filebinding.ParseMain。

 
