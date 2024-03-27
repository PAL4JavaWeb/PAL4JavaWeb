


import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class chatGpt {
    public static List<String> parseArray(String input) {//取出返回内容的数组
        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            String arrayString = matcher.group(1);
            String[] elements = arrayString.split(",");
            for (String element : elements) {
                String value = element.trim();
                list.add(value);
            }
        }
        return list;
    }


    public static Map<String, Object> extractAndParseDictionary(String jsonString) {
        try {
            int startIndex = jsonString.indexOf("{");
            int endIndex = jsonString.lastIndexOf("}");

            if (startIndex != -1 && endIndex != -1) {
                    String dictionaryString = jsonString.substring(startIndex, endIndex + 1);
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(dictionaryString, Map.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String chatGptApiForFrontEnd(Proxy proxy, OpenAiClient openAiClient, String content){
        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，"+content+"以上数组的元素是项目的目录的一部分，请按" +
                "照下面的步骤回复：" +
                "step 1：判断输入的数组元素是否为前端文件；" +
                "step 2：如果是，从数组中可以体现这个项目用了什么前端框架或技术；" +
                "step 3：将step 2中得到的框架或技术用数组的形式返回").build();

//        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，"+content+"以上数组的元素是项目的目录的一部分，请按" +
//                "照下面的步骤回复：" +
//                "step 1：从数组中可以体现这个项目用了什么前端框架或技术；" +
//                "step 2：将step 2中得到的框架或技术用数组的形式返回").build();

//        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，"+content+"以上数组的元素是项目的目录的一部分，请按" +
//                "照下面的步骤回复：" +
//                "step 1：判断输入的数组元素是否为前端文件；" +
//                "step 2：从数组中可以体现这个项目用了什么前端框架或技术；" +
//                "step 3：将结果用数组的形式返回").build();

//        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，"+content+
//                "以上数组的元素是项目的目录的一部分，请帮忙判断这些元素体现这个项目用了什么前端框架或技术，用数组的形式返回。").build();

//        Message message2 = Message.builder().role(Message.Role.USER).content(content).build();
//        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message1,message2)).build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message1)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);

        return chatCompletionResponse.getChoices().get(0).getMessage().getContent();
    }

    public static String chatGptApiForBackEndCallgraph(Proxy proxy, OpenAiClient openAiClient,String content){
        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，我将给你一些代码间调用的参数，请按\n" +
                "照下面的步骤回复：\n" +
                "step 1：哪些参数是有关数据库的。\n" +
                "step 2：这个项目用了什么数据库。\n" +
                "step 3：请以JSON的形式回答，键包括database，url，username，password，没有就填null。").build();

//        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，我将给你一些代码间调用的参数，请按\n" +
//                "这个项目用了什么数据库。请以JSON的形式回答，键包括database，url，username，password，没有就填null。").build();

        Message message2 = Message.builder().role(Message.Role.USER).content(content).build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message1,message2)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);

        return chatCompletionResponse.getChoices().get(0).getMessage().getContent();
    }

    public static String chatGptApiForBackEndCode(Proxy proxy, OpenAiClient openAiClient, String content){
        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，我将给你一个项目的其中一部分配置文件的内容，请按" +
                "照下面的步骤回复：\n" +
                "step 1：从这个配置文件中获得了什么信息。\n" +
                "step 2：这个项目用了哪些数据库，数据库的名称是什么，项目的端口号是什么，用了什么后端框架，如果有redis，也请列出来。\n" +
                "step 3：如果要修改将这些数据库连接至本地，该创建什么名字的数据库，修改哪些配置，如果有redis，也请加入Database这个键中。\n" +
                "step 4：请以JSON的形式回答，键包括：\n" +
                "Database,url,dbname,modified configuration,server-port,back-end framework，server-prot没有就写8080，其余的没有就写null。").build();

//        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，我将给你一个项目的其中一部分配置文件的内容，请按" +
//                "照下面的步骤回复：\n" +
//                "step 1：这个项目用了哪些数据库，数据库的名称是什么，项目的端口号是什么，用了什么后端框架，如果有redis，也请列出来。\n" +
//                "step 2：如果要修改将这些数据库连接至本地，该创建什么名字的数据库，修改哪些配置，如果有redis，也请加入Database这个键中。\n" +
//                "step 3：请以JSON的形式回答，键包括：\n" +
//                "Database,url,dbname,modified configuration,server-port,back-end framework，server-prot没有就写8080，其余的没有就写null。").build();

//                Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，我将给你一个项目的其中一部分配置文件的内容，请按" +
//                "照下面的步骤回复：\n" +
//                "step 1：从这个配置文件中获得了什么信息。\n" +
//                "step 2：这个项目用了哪些数据库，数据库的名称是什么，项目的端口号是什么，用了什么后端框架，如果有redis，也请列出来。\n" +
//                "step 3：如果要修改将数据库连接至本地，该创建什么名字的数据库，修改哪些配置，如果有redis，也请加入Database这个键中。\n" +
//                "step 4：请以JSON的形式回答，键包括：\n" +
//                "Database,url,dbname,modified configuration,server-port,back-end framework，server-prot没有就写8080，其余的没有就写null。").build();

//                Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，我将给你一个项目的其中一部分配置文件的内容，请按" +
//                "照下面的步骤回复：\n" +
//                "step 1：从这个配置文件中获得了什么信息。\n" +
//                "step 2：这个项目用了什么数据库，数据库的名称是什么，项目的端口号是什么，用了什么后端框架。\n" +
//                "step 3：如果要修改将step 2的数据库连接至本地，该创建什么名字的数据库，修改哪些配置。\n" +
//                "step 4：请以JSON的形式回答，键包括：\n" +
//                "Database,url,dbname,modified configuration,server-port,back-end framework，server-prot没有就写8080，其余的没有就写null。").build();

//        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，我将给你一个项目的其中一部分配置文件的内容，请帮我判断" +
//                "如何配置项目的后端数据库才能使程序正常运行，请以JSON的形式回答，键包括：\n" +
//                "Database,url,dbname,modified configuration,server-port,back-end framework，server-prot没有就写8080，其余的没有就写null。").build();

        Message message2 = Message.builder().role(Message.Role.USER).content(content).build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message1,message2)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);

        return chatCompletionResponse.getChoices().get(0).getMessage().getContent();
    }
    public static List<File> findFilesWithExtensions(File directory, String... extensions) {
        List<File> result = new ArrayList<>();
        Queue<File> queue = new LinkedList<>();

        if (!directory.isDirectory()) {
            return result;
        }

        queue.add(directory);

        while (!queue.isEmpty()) {
            File currentDirectory = queue.poll();

            File[] files = currentDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.getAbsolutePath().contains("_jacg_sql") && file.isFile() && !isIgnoredDirectory(file) && endsWithExtensions(file.getName(), extensions)) {
                        result.add(file);
                    } else if (file.isDirectory() && !isIgnoredDirectory(file)) {
                        queue.add(file);
                    }
                }
            }
        }

        return result;
    }

    private static boolean endsWithExtensions(String fileName, String[] extensions) {
        for (String extension : extensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isIgnoredDirectory(File file) {
        String name = file.getName();
        return name.equals("node_modules") || name.equals("target") || name.equals(".idea") || name.equals("test");
    }

    public static String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    public static String chatGptApiForFindBackEndCode(Proxy proxy, OpenAiClient openAiClient, List<File> files){
        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("我将给你一些javaweb的配置文件名称，" +
                "请帮我判断一下哪些文件有可能包含后端数据库的配置，" +
                "并将最有可能包含数据库配置前三个文件用数组的形式告诉我，回复的格式如下：" +
                "根据给出的文件路径，可能包含后端数据库配置的文件有" +
                "[" +
                "D:\\\\Desktop\\\\javaweb\\\\bicycleSharingServer-master\\\\src\\\\main\\\\resources\\\\application.properties," +
                "D:\\\\Desktop\\\\javaweb\\\\bicycleSharingServer-master\\\\src\\\\main\\\\resources\\\\jdbc.properties," +
                "D:\\\\Desktop\\\\javaweb\\\\bicycleSharingServer-master\\\\src\\\\main\\\\resources\\\\redis.properties" +
                "]").build();
        Message message2 = Message.builder().role(Message.Role.USER).content(files.toString()).build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message1,message2)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);

        return chatCompletionResponse.getChoices().get(0).getMessage().getContent();
    }

    public static String chatGptApiForWholeProject(Proxy proxy, OpenAiClient openAiClient,String content){
        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，"+content+",以上是一段信息，里面是一个项目的配置信息，你的任务是利用这些信息帮我生成这个项目的运行步骤，请按照下面的步骤回复：\n" +
                "step 1：将这个信息转换成json格式。\n"+
                "step 2：根据第一步的结果，生成这个javaweb项目的运行步骤，最后是一组案例，请按照案例输出的格式生成运行步骤，" +
                "确保涵盖下面所有名词：" +
                        "其中front-end是前端配置，back-end-config是关于后端数据库连接的配置文件，back-end是后端配置，database是用到的数据库，" +
                        "url是数据库的连接地址，dbname是需要创建的数据库的名称， modified configuration是需要做出修改的数据库配置，" +
                        "sever-port是启动项目ip地址的端口号，back-end framework是用到的后端框架，" +
                        "sql-file是需要导入数据库的文件，start-method是运行整个项目的方式。\n" +
                "案例输入：{\n" +
                "front-end:['element-ui', 'echarts', 'vue', 'webpack', 'tinymce'],\n" +
                "back-end-config:[D:\\Desktop\\javaweb\\qinlouyue-master\\qinlouyue-api\\src\\main\\resources\\application-dev.properties, D:\\Desktop\\javaweb\\qinlouyue-master\\qinlouyue-api\\src\\main\\resources\\application-prod.properties, D:\\Desktop\\javaweb\\qinlouyue-master\\qinlouyue-api\\src\\main\\resources\\application.properties],\n" +
                "back-end:{Database=[MySQL, MongoDB], url=[jdbc:mysql://my-db:3306/qinlouyue?useUnicode=true&characterEncoding=UTF8, mongodb://my-db:27017/qinlouyue], dbname=[qinglouyue, qinglouyue], modified configuration=[spring.datasource.url, spring.datasource.username, spring.datasource.password, spring.data.mongodb.uri, spring.data.mongodb.username, spring.data.mongodb.password], server-port=8082, back-end framework=null},\n" +
                "sql-file:[D:\\Desktop\\javaweb\\qinlouyue-master\\qinlouyue-api\\src\\main\\resources\\import.sql],\n" +
                "start-method:直接点击Application.java启动\n" +
                "}" +
                "案例输出：" +
                "1.前端配置：\n" +
                "项目使用了 element-ui、echarts、vue、webpack 和 tinymce。确保这些前端库已正确引入和配置。\n" +
                "命令行输入npm run dev\n" +
                "2.后端配置：\n" +
                "项目使用了 MySQL 和 MongoDB 数据库。您需要在 application-dev.properties、application-prod.properties、application.properties 文件中配置数据库连接信息。\n" +
                "MySQL 配置：\n" +
                "数据库连接URL：jdbc:mysql://localhost:3306/qinglouyue?useUnicode=true&characterEncoding=UTF8\n" +
                "用户名：root\n" +
                "密码：123456\n" +
                "MongoDB 配置：\n" +
                "数据库连接URI：mongodb://localhost:27017/qinglouyue\n" +
                "用户名：enilu\n" +
                "密码：enilu123456\n" +
                "server-port配置：\n" +
                "网站端口号为8082，启动成功后访问 http://localhost:8082即可进入网站，用户名和密码在数据库的用户信息表中。\n" +
                "3.后端框架：\n" +
                "后端框架是 springboot 框架。\n" +
                "4.导入 SQL 文件：\n" +
                "项目需要导入 import.sql 文件到数据库中。您可以使用数据库管理工具或命令行工具执行该 SQL 文件。\n" +
                "5.启动项目：\n" +
                "项目的启动方式是直接点击 Application.java 文件启动。确保您的开发环境配置正确，项目的依赖项已经安装。"
        ).build();
//                "step 2：根据第一步的结果，按照以下步骤生成这个javaweb项目的运行步骤，其中front-end是前端配置，" +
//                "back-end是后端配置，database是用到的数据库，url是数据库的连接地址，dbname是需要创建的数据库的名称，" +
//                "modified configuration是需要做出修改的数据库配置，sever-port是启动项目ip地址的端口号，" +
//                "back-end framework是用到的后端框架，sql-file是需要导入数据库的文件，start-method是运行整个项目的方式。").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message1)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);

        return chatCompletionResponse.getChoices().get(0).getMessage().getContent();
    }

    public static String runProject(String projectRootPath) throws IOException {
        // 项目根目录
//        String projectRootPath = "path/to/project/root"; // 替换为实际项目根目录的路径

        // 递归查找文件
        File projectRootDir = new File(projectRootPath);
        // ① 查找项目文件中是否有文件名带有application.java的文件
        File applicationJavaFile = findFileWithApplicationJava(projectRootDir, "Application");

        if (applicationJavaFile != null) {
            // ② 如果有，输出“直接点击运行”
            System.out.println("直接点击Application.java启动");
            return "直接点击Application.java启动";

        } else {
            // ② 如果没有，找pom.xml
            File pomXmlFile = findPomXmlFile(projectRootPath);

            if (pomXmlFile == null) {
                System.out.println("没有pom.xml");
                // 如果没有pom.xml，输出“直接在本地下载tomcat，在编译器中手动将项目打包成.war的形式，并且在编译器中选择使用tomcat运行这个war包”
                System.out.println("直接在本地下载tomcat，在编译器中手动将项目打包成.war的形式，并且在编译器中选择使用tomcat运行这个war包");
                return "没有pom.xml,直接在本地下载tomcat，在编译器中手动将项目打包成.war的形式，并且在编译器中选择使用tomcat运行这个war包";
            } else {
                System.out.println("有pom.xml");

                // 如果有pom.xml，读取内容并查找<package>标签
                String packageType = getPackageTypeFromPomXml(pomXmlFile);

                if ("jar".equals(packageType)) {
                    // 如果<package>标签内容是“jar”，输出“运行指令是mvn clean package run”
                    System.out.println("运行指令是mvn clean package run");
                    return "有pom.xml,运行指令是mvn clean package run";

                } else if ("war".equals(packageType)) {
                    // 如果<package>标签内容是“war”，查找服务器插件
                    boolean hasTomcatPlugin = hasTomcatPluginInPomXml(pomXmlFile);

                    if (hasTomcatPlugin) {
                        // 如果有服务器插件，输出“运行指令是tomcat7:run”
                        System.out.println("运行指令是tomcat7:run");
                        return "有pom.xml,运行指令是tomcat7:run";

                    } else {
                        // 如果没有服务器插件，输出“本地下载tomcat，在编译器中手动打包运行”
                        System.out.println("本地下载tomcat，在编译器中手动打包运行");
                        return "有pom.xml,本地下载tomcat，在编译器中手动打包运行";
                    }
                }else {
                    // 如果<package>标签内容是“war”，查找服务器插件
                    boolean hasTomcatPlugin = hasTomcatPluginInPomXml(pomXmlFile);

                    if (hasTomcatPlugin) {
                        // 如果有服务器插件，输出“运行指令是tomcat7:run”
                        System.out.println("运行指令是tomcat7:run");
                        return "有pom.xml,运行指令是tomcat7:run";
                    }
                }
            }
        }
        return "无法判断";
    }

    // 查找项目文件中是否有文件名带有application.java的文件
    private static File findFileWithApplicationJava(File directory, String fileName) {
        // 实现查找文件的逻辑，返回找到的文件对象，如果没找到返回null
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().contains(fileName)) {
                    return file;
                } else if (file.isDirectory()) {
                    File foundFile = findFileWithApplicationJava(file, fileName);
                    if (foundFile != null) {
                        return foundFile;
                    }
                }
            }
        }
        return null; // 文件未找到
    }

    public static List<String> findMainMethodFiles(File directory) {
        List<String> mainMethodFiles = new ArrayList<>();
        if (!directory.isDirectory()) {
            return mainMethodFiles;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    mainMethodFiles.addAll(findMainMethodFiles(file));
                } else if (file.getName().endsWith(".java")) {
                    try {
                        List<String> lines = Files.readAllLines(file.toPath());
                        for (String line : lines) {
                            if (line.contains("public static void main(String[] args)")) {
                                mainMethodFiles.add(file.getAbsolutePath());
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mainMethodFiles;
    }

    // 查找pom.xml文件
    private static File findPomXmlFile(String projectRootPath) {
        // 实现查找文件的逻辑，返回找到的文件对象，如果没找到返回null
        // 项目根目录
//        String projectRootPath = "path/to/project/root"; // 替换为实际项目根目录的路径
        // 查找项目主目录下的 pom.xml 文件
        File projectRootDir = new File(projectRootPath);
        File pomXmlFile = new File(projectRootDir, "pom.xml");

        if (pomXmlFile.exists() && pomXmlFile.isFile()) {
            return pomXmlFile;
        } else {
            return null;
        }
    }

    // 读取pom.xml中的<package>标签内容
    private static String getPackageTypeFromPomXml(File pomXmlFile) {
        String packageType = ""; // 初始化为空

        try (BufferedReader reader = new BufferedReader(new FileReader(pomXmlFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 在每行中查找 <package> 标签
                if (line.contains("<packaging>")) {
                    int startIndex = line.indexOf("<packaging>") + "<packaging>".length();
                    int endIndex = line.indexOf("</packaging>");
                    packageType = line.substring(startIndex, endIndex).trim();
                    break; // 找到标签后，停止查找
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return packageType;
    }

    // 检查pom.xml中是否有tomcat7或其他服务器插件
    private static boolean hasTomcatPluginInPomXml(File pomXmlFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pomXmlFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 在每行中查找 tomcat7 或其他服务器插件
                if (line.contains("tomcat7") || line.contains("jetty")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // 未找到相关插件
    }

    public static String mergeAndCleanFiles(List<String> filePaths) throws IOException {
        StringBuilder mergedContent = new StringBuilder();

        for (String filePath : filePaths) {
            String fileContent = readFileContent2(filePath);
            String cleanedContent = cleanFileContent(fileContent);
            mergedContent.append(cleanedContent);
        }

        return mergedContent.toString();
    }

    private static String readFileContent2(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    private static String cleanFileContent(String content) {
        String[] lines = content.split("\\R");
        StringBuilder cleanedContent = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                cleanedContent.append(line).append(System.lineSeparator());
            }
        }

        return cleanedContent.toString();
    }

    public static void writeToTextFile(String content, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(content);
            writer.newLine();  // 换行，可选
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getFileNames(File file, List<String> fileNames) {
        File[] files = file.listFiles();
        if (file.getName().equals("node_modules")){
            String name[] = file.list();
            for (String f:name){
                fileNames.add(f);
            }
        }
        else{
            for (File f : files) {
                if (f.isDirectory()) {
                    fileNames.add(f.getName());
                    getFileNames(f, fileNames);
                } else {
                    fileNames.add(f.getName());
                }
            }
        }


        return duplicateListBySet(fileNames);
    }

    public static List<String> duplicateListBySet(List<String> list) {//去重
        HashSet h = new HashSet(list);
        List newList = new ArrayList();
        newList.addAll(h);
        return newList;
    }

    public static List<String> Ebstract(List<String> dir,List<String> characteristic){

        List<String> describe = new ArrayList<>(dir);
        List<String> mainFrontFile = new ArrayList<>();
        for(String file:describe){
            if(file.endsWith(".jsp")||file.endsWith(".js")||file.endsWith(".html")){
                mainFrontFile.add(file);
            }
        }
        describe.retainAll(characteristic);
        describe.addAll(mainFrontFile);
        return describe;
    }

    private static class properties{
        public Map<String, Object> getBackend() {
            return backend;
        }

        public void setBackend(Map<String, Object> backend) {
            this.backend = backend;
        }

        public List<String> getFrontend() {
            return frontend;
        }

        public void setFrontend(List<String> frontend) {
            this.frontend = frontend;
        }

        public List<File> getSqlFile() {
            return sqlFile;
        }

        public void setSqlFile(List<File> sqlFile) {
            this.sqlFile = sqlFile;
        }

        public String getStartMethod() {
            return startMethod;
        }

        public void setStartMethod(String startMethod) {
            this.startMethod = startMethod;
        }

        private Map<String, Object> backend;
        private List<String> frontend;
        private List<File> sqlFile;
        private String startMethod;

        public List<String> getBackend_config() {
            return backend_config;
        }

        public void setBackend_config(List<String> backend_config) {
            this.backend_config = backend_config;
        }

        private List<String>backend_config;

        @Override
        public String toString() {
            return "{\n"
                    + "front-end:" + frontend + ",\n"
                    + "back-end-config:" + backend_config +",\n"
                    + "back-end:" + backend + ",\n"
                    + "sql-file:" + sqlFile + ",\n"
                    + "start-method:" + startMethod + "\n"
                    + "}";
        }
    }

    public static class Tree {
        private String id;
        private String pid;
        private int level;
        private List<Tree> children;
        private List<Tree> parent;
        private String functionName;
        private List<String> param;

        public List<Tree> getParent() {
            return parent;
        }

        public void setParent(List<Tree> parent) {
            this.parent = parent;
        }

        public List<String> getParam() {
            return param;
        }

        public void setParam(List<String> param) {
            this.param = param;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public List<Tree> getChildren() {
            return children;
        }

        public void setChildren(List<Tree> children) {
            this.children = children;
        }

        public String getFunctionName() {
            return functionName;
        }

        public void setFunctionName(String functionName) {
            this.functionName = functionName;
        }


        public Tree() {
            super();
        }
        @Override
        public String toString() {
            return "Tree [level=" + level + ", id=" + id + ", pid="+ pid+", functionName="
                    + functionName + ", param=" + param + "]"+ ", parents=" + parent + "]\n";
        }

    }

    /*
     * @version V1.0
     * Title: builTree
     * @author LiuYanQiang
     * @description 始树形结构创建
     * @createTime  2022/5/3 0:18
     * @param [list]
     * @return java.util.List<com.lyq.generateTree.Tree>*/
    public List<Tree> builTree(List<Tree> list) {
        List<Tree> treeList = new ArrayList<>();
        for (Tree tree : getRootNode(list)) {
            //建立子树节点
            tree = buildChilTree(tree,list,0);
            //为根节点设置子树节点
            treeList.add(tree);
        }
        return treeList;
    }
    /*
     * @version V1.0
     * Title: getRootNode
     * @author LiuYanQiang
     * @description 获取全部根节点
     * @createTime  2022/5/3 0:18
     * @param [list]
     * @return java.util.List<com.lyq.generateTree.Tree>*/
    private List<Tree> getRootNode(List<Tree> list) {
        List<Tree> rootList = new ArrayList<>();
        for (Tree tree : list) {
            if (tree.getPid().equals("null")) {
                rootList.add(tree);
            }
        }
        return rootList;
    }

    /*
     * @version V1.0
     * Title: buildChilTree
     * @author LiuYanQiang
     * @description 通过递归来创建子树形结构
     * @createTime  2022/5/3 0:18
     * @param [tree, list]
     * @return com.lyq.generateTree.Tree*/
    private Tree buildChilTree(Tree tree, List<Tree> list, int level) {
        List<Tree> treeList = new ArrayList<>();

        for (Tree t : list) {
            List<Tree> treeParents = new ArrayList<>();
            int nextlevel=level+1;
            //判断当前父节点是否存在子节点
            if (t.getLevel()==nextlevel&&tree.getId().contains(t.getPid())) {
                treeParents.add(tree);
                t.setParent(treeParents);

                treeList.add(t);
                buildChilTree(t,list,nextlevel);
            }
        }
        tree.setChildren(treeList);

        return tree;
    }

    public static List readFileContent3(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        List<Tree> list = new ArrayList<Tree>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String readStr;
            while ((readStr = reader.readLine()) != null) {
                Tree tree = new Tree();
                readStr = readStr.replaceAll("\\s+"," ");//将长空格替换成一个空格
                String[] arr=readStr.split(" ");
                tree.setPid(arr[1]);//调用者放在pid
                tree.setId(arr[2]);//被调用者放在id
                tree.setFunctionName(arr[3]);//行号暂时借放在这
                list.add(tree);
            }
            reader.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return list;
    }


    public static String traceTree(List<Tree> list, String s){//遍历
        int i=0;
        for(Tree t:list){
            if(!t.getChildren().isEmpty()){
                if(t.getLevel()==0){
                    System.out.println("\""+t.getFunctionName()+"\":{");
                    s+=("\""+t.getFunctionName()+"\":{");
                    s=traceTree(t.getChildren(), s);
                }
                else{
                    System.out.println("\""+t.getParam()+i+"\":{\""+t.getFunctionName()+"\":{");
                    s+=("\""+t.getParam()+i+"\":{\""+t.getFunctionName()+"\":{");
                    s=traceTree(t.getChildren(), s);
                    s+="}";
                    System.out.println("}");
                }
                s+="},";
                System.out.println("},");
                i++;
            }
            else{
                s+=("\""+t.getParam()+i+"\":\""+t.getFunctionName()+"\",");
                System.out.println("\""+t.getParam()+i+"\":\""+t.getFunctionName()+"\",");
                i++;
            }
        }
        return s;
    }

    public  static void getParams(List<Tree> list, List<List<String>> paramList){//遍历有好几颗树的列表得到所有参数
        for(Tree t:list) {
            if (t.getChildren().size() > 0) {
                getParams(t.getChildren(), paramList);
            }
            if (t.getParam() != null) {
                System.out.println(t.getParam());
                paramList.add(t.getParam());
            }
        }
    }

    public static String methodcall_path =
            "D:\\Desktop\\javaweb\\mytwitter-master\\classes\\artifacts\\mytwitter_war\\mytwitter_war.war-output_javacg\\method_call.txt";


    public static List<Tree> readTxts(String path) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "gbk");
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        int count = 0;
        List<Tree> list = new ArrayList<Tree>();
        List<Tree> methodList = readFileContent3(methodcall_path);
        while ((line = br.readLine()) != null) {
           Tree txt = new Tree();

            if(line.contains("method_call_info")){
                String[] arr = line.split("!ext_data!");
                Pattern p2 = Pattern.compile("\\[.+?\\]");//找出所有中括号中的参数
                Matcher m2 = p2.matcher(arr[1]);
                List<String> result2 = new ArrayList<>();
                while(m2.find()){
                    result2.add(m2.group());
                }
                //System.out.println(result2);
                txt.setParam(result2);
                line = arr[0];
            }

            line = line.replaceAll("\\s+"," ");//将长空格替换成一个空格
            String[] arr = line.split(" ");
            if(count==0){
                txt.setId(arr[0]);
                txt.setPid("null");
                txt.setLevel(0);
                txt.setFunctionName(arr[0]);
                list.add(txt);
            }
            else if (arr.length == 3) {
                txt.setId(arr[2]);

                Pattern p1 = Pattern.compile("[^0-9]");//滤出纯层级
                Matcher m1 = p1.matcher(arr[0]);
                String result1 = m1.replaceAll("").trim();

                Pattern p = Pattern.compile("[^(0-9)]");
                Matcher m = p.matcher(arr[1]);//选中行号
                String result = m.replaceAll("").trim();//删掉

                //待完成：arr[2]和result一起去methodcall里找替换的父节点
                for (int i = 0; i < methodList.size(); i++) {
                    if(methodList.get(i).getId().contains(arr[2])&&result.equals(methodList.get(i).getFunctionName())){
                        txt.setPid(methodList.get(i).getPid());
                    }
                }


                txt.setLevel(Integer.parseInt(result1));


                txt.setFunctionName(arr[2]);
                list.add(txt);
            }
            count++;
        }
//        System.out.println(list);
//        System.out.println("读取总条数：" + count + "||读取的list的长度" + list.size());
        return list;
    }

    public static List<Tree> buildTreeFromFolder(String folderPath) throws IOException {//把文件夹下所有文件生成树放在一个列表中，遍历列表找出参数
        List<Tree> list1 = new ArrayList<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        });
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    List<Tree> list2 = new ArrayList<>();
                    list2 = readTxts(file.getAbsolutePath());
                    list2 = new chatGpt().builTree(list2);
                    list1.addAll(list2);
                }
            }
        }

        return list1;
    }




    public static void main(String[] args) throws IOException, InterruptedException {
//        //代理可以为null
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 4780));
//        OpenAiClient openAiClient = OpenAiClient.builder()
////                .apiKey("sk-**")
//                .apiKey(Arrays.asList("sk-**"))
////                .proxy(proxy)
//                .build();
//        //简单模型
//        //CompletionResponse completions = //openAiClient.completions("我想申请转专业，从计算机专业转到会计学专业，帮我完成一份两百字左右的申请书");
//        //最新GPT-3.5-Turbo模型
////        Message message = Message.builder().role(Message.Role.USER).content("你好啊我的伙伴！").build();

        //国内访问需要做代理，国外服务器不需要
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 4780));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .proxy(proxy)//自定义代理
                .addInterceptor(httpLoggingInterceptor)//自定义日志
                .connectTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                .writeTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                .readTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                .build();

        //构建客户端
        OpenAiClient openAiClient = OpenAiClient.builder()
                .apiKey(Arrays.asList("sk-***"))
                //自定义key的获取策略：默认KeyRandomStrategy
                .keyStrategy(new KeyRandomStrategy())
//                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传
//                .apiHost("https://自己代理的服务器地址/")
                .build();


        String projname = "D:\\Desktop\\RQ3\\shopping-management-system-master\\3#Java狙击项目\\fbird";
        String projname2 = "D:\\Desktop\\javaweb\\SpringBoot-Admin-master";
        properties properties = new properties();

        List<String> characteristic = new ArrayList<>();
        characteristic = Arrays.asList("App.vue","vue.js","main.js","package.json","node_modules","App.js","index.js","angular.json","tsconfig.app.json","main.ts"
                ,"app.component,ts","tsconfig.spec.json","karma.conf.js","App.svelte","Button.svelte","rollup.config.js","environment.js",".ember-cli",".jshintrc"
                ,"ember-cli-build.js","index.html","resolver.js","router.js","package-lock.json","preact","lit.js","yarn.lock","bootstrap.js","jquery.js"
                ,"jquery.min.js","semantic.min.js","element-ui","antd","vuetity","layui.js","echarts","echarts.js","echarts.min.js");

        List<String> describe;
        File files = new File(projname);
        List<String> fileNames = new ArrayList<>();
        fileNames = getFileNames(files, fileNames);
        System.out.println(fileNames);
        describe = Ebstract(fileNames,characteristic);
        System.out.println(describe);
        String result;
        try {
            result = chatGptApiForFrontEnd(proxy,openAiClient,describe.toString());
            System.out.println(result);
            List<String>front_end;
            front_end = parseArray(result);
            System.out.println(front_end);
            properties.setFrontend(front_end);
        } catch (RuntimeException e){
            e.printStackTrace();
        }

//        try {
//            List<Tree> list1 = new ArrayList<>();
//            list1 = buildTreeFromFolder("D:\\Desktop\\javaweb\\mytwitter-master\\_jacg_o_er\\20230920-150240.857");
//            List<List<String>> paramList = new ArrayList<>();
//            getParams(list1, paramList);
//            System.out.println(paramList);
//            String answer1;
//            answer1 = chatGptApiForBackEndCallgraph(proxy,openAiClient,paramList.toString());
//            System.out.println(answer1);
//            System.out.println(extractAndParseDictionary(answer1));
//            Map r = extractAndParseDictionary(answer1);
//            r.put("server-port","8080");
//            properties.setBackend(r);
//        }catch (IOException e) {
//            e.printStackTrace();
//        }


        try {
            File projectFolder = new File(projname);
            String[] extensions = {".properties",".yaml", ".yml"};
            List<File> files1 = findFilesWithExtensions(projectFolder, extensions);
            System.out.println(files1);
            String result1;
            result1 = chatGptApiForFindBackEndCode(proxy,openAiClient,files1);
            System.out.println(result1);
            List<String>backend_config = new ArrayList<>();

//        backend_config.add("D:\\Desktop\\javaweb\\My-Blog-master\\src\\main\\resources\\application.properties");
//        backend_config.add("D:\\Desktop\\javaweb\\My-Blog-master\\src\\main\\resources\\mapper\\AdminUserMapper.xml");

            backend_config = parseArray(result1);
            System.out.println(backend_config);
            properties.setBackend_config(backend_config);

            String mergedContent = mergeAndCleanFiles(backend_config);
            // 截取前两千个字符
            String truncatedString = mergedContent.substring(0, Math.min(mergedContent.length(), 2000));


//            Thread.sleep(10000);

            System.out.println("Merged and cleaned content:");
            System.out.println(truncatedString);
            String answer = chatGptApiForBackEndCode(proxy,openAiClient,truncatedString);
            System.out.println(answer);
            System.out.println(extractAndParseDictionary(answer));
            properties.setBackend(extractAndParseDictionary(answer));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String startMethod = runProject(projname);
        properties.setStartMethod(startMethod);
        String[] extensions1 = {".sql", ".db"};
        List<File> sqlFile;
        sqlFile = findFilesWithExtensions(new File(projname),extensions1);
        System.out.println(sqlFile);
        properties.setSqlFile(sqlFile);

//        System.out.println(properties.toString());
//        writeToTextFile(properties.toString(),"D:\\Desktop\\result.txt");

        String inicontent = properties.toString();

//        String inicontent = "gd";

        System.out.println(inicontent);
        String wholeResult = chatGptApiForWholeProject(proxy,openAiClient,inicontent);
        System.out.println(wholeResult);

//        writeToTextFile("\n"+projname,"D:\\Desktop\\result.txt");
//        writeToTextFile(inicontent,"D:\\Desktop\\result.txt");
//        writeToTextFile(wholeResult,"D:\\Desktop\\result.txt");



    }
}

