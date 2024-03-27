import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RQ2Baseline1 {
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
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message1)).build();
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

    public static String chatGptApiForFindBackEndCode(Proxy proxy, OpenAiClient openAiClient,List<File> files){
        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("我将给你一些javaweb的配置文件名称，" +
                "请帮我判断一下哪些文件有可能包含后端数据库的配置，" +
                "并将最有可能包含数据库配置前三个文件用数组的形式告诉我，回复的格式如下：" +
                "根据给出的文件路径，可能包含后端数据库配置的文件有" +
                "[" +
                "D:\\\\Desktop\\\\javaweb\\\\myadmin-master\\\\src\\\\main\\\\resources\\\\application.properties," +
                "D:\\\\Desktop\\\\javaweb\\\\myadmin-master\\\\src\\\\main\\\\resources\\\\jdbc.properties," +
                "D:\\\\Desktop\\\\javaweb\\\\myadmin-master\\\\src\\\\main\\\\resources\\\\redis.properties" +
                "]"
        ).build();
        Message message2 = Message.builder().role(Message.Role.USER).content(files.toString()).build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message1,message2)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);

        return chatCompletionResponse.getChoices().get(0).getMessage().getContent();
    }

    public static String chatGptApiForStartMethod(Proxy proxy, OpenAiClient openAiClient, String content){
        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("请你扮演一个资深的程序员，"+content+"以上代码是一个项目的pom.xml文件，请按" +
                "照下面的步骤回复：" +
                "step 1：请找出关于项目启动方式的信息" +
                "step 2：请推测出这个项目的启动方式，需要用到什么操作或者命令语句" +
                "step 3：将step 2中得到的结果用数组的形式返回").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message1)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);

        return chatCompletionResponse.getChoices().get(0).getMessage().getContent();
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

    private static List<String> listFiles(String directoryPath) {
        List<String> fileNames = null;

        try {
            Path directory = Paths.get(directoryPath);

            // 使用 Files.list 获取文件列表
            fileNames = Files.list(directory)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNames;
    }

    public static List<String> duplicateListBySet(List<String> list) {//去重
        HashSet h = new HashSet(list);
        List newList = new ArrayList();
        newList.addAll(h);
        return newList;
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

        public List<String> getStartMethod() {
            return startMethod;
        }

        public void setStartMethod(List<String> startMethod) {
            this.startMethod = startMethod;
        }

        private Map<String, Object> backend;
        private List<String> frontend;
        private List<File> sqlFile;
        private List<String> startMethod;

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
                    + "\"front-end\":" + frontend + ",\n"
                    + "\"back-end-config\":" + backend_config +",\n"
                    + "\"back-end\":" + backend + ",\n"
                    + "\"start-method\":" + startMethod + "\n"
                    + "}";
        }
    }

    public static void main(String[] args) throws IOException {
        //代理可以为null
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 4780));
        OpenAiClient openAiClient = OpenAiClient.builder()
//                .apiKey("sk-**")
                .apiKey(Arrays.asList("sk-**"))
//                .proxy(proxy)
                .build();

        String directoryPath = "D:\\Desktop\\javaweb\\";

        List<String> fileNames = listFiles(directoryPath);

        for (String projname:fileNames){
            File projectFolder = new File(directoryPath+projname);
            if (projectFolder.getName().equals("desktop.ini"))continue;
            System.out.println("开始处理"+projectFolder.getName());
            writeToTextFile(projectFolder.getName()+":","D:\\Desktop\\RQ2-Baseline0.txt");
            properties properties = new properties();
            //前端
            String[] front_extensions = {".js", ".html",".css"};
            List<String>front_end_framework = new ArrayList<>();
            List<File> front_end_file = findFilesWithExtensions(projectFolder,front_extensions);
            List<String>front_end_filename = new ArrayList<>();
            for (File file:front_end_file){
                front_end_filename.add(file.getName());
            }
            front_end_filename = duplicateListBySet(front_end_filename);
            System.out.println(front_end_filename);
            String front_result;
            try {
                front_result = chatGptApiForFrontEnd(proxy,openAiClient,front_end_filename.toString());
                System.out.println(front_result);
                List<String>front_end;
                front_end = parseArray(front_result);
                System.out.println(front_end);
                properties.setFrontend(front_end);
            } catch (RuntimeException e){
                e.printStackTrace();
            }

            //后端
            String[] back_extensions = {".java",".properties",".yml","yaml"};
            List<String>back_end_framework = new ArrayList<>();
            List<File> back_end_file = findFilesWithExtensions(projectFolder,back_extensions);
            System.out.println(back_end_file.size());
            String back_result;
            int batchSize = 40;
            List<File>backend_config = new ArrayList<>();
            // 计算总批数
            int totalBatches = (int) Math.ceil((double) back_end_file.size() / batchSize);
            // 循环处理每个批次
            try {
                for (int batchNumber = 0; batchNumber < totalBatches; batchNumber++) {
                    // 计算当前批次的起始和结束索引
                    int startIndex = batchNumber * batchSize;
                    int endIndex = Math.min((batchNumber + 1) * batchSize, back_end_file.size());
                    // 获取当前批次的数据子列表
                    List<File> currentBatch = back_end_file.subList(startIndex, endIndex);
                    // 处理当前批次的数据
                    back_result = chatGptApiForFindBackEndCode(proxy,openAiClient,currentBatch);
                    System.out.println(back_result);
                    List<String>backend_config1 = new ArrayList<>();
                    backend_config1 = parseArray(back_result);
                    //每个批次识别结果加入总列表中
                    if (!backend_config1.isEmpty()){
                        for (String file: backend_config1){
                            backend_config.add(new File(file));
                        }
                        System.out.println(backend_config);
                    }
                }
                System.out.println(backend_config);
                //对总列表做最后的筛选
                back_result = chatGptApiForFindBackEndCode(proxy,openAiClient,backend_config);
                List<String>backend_config2 = parseArray(back_result);
                System.out.println(backend_config2);
                properties.setBackend_config(backend_config2);
                String mergedContent = mergeAndCleanFiles(backend_config2);
                // 截取前两千个字符
                String truncatedString = mergedContent.substring(0, Math.min(mergedContent.length(), 2000));
                System.out.println("Merged and cleaned content:");
                System.out.println(truncatedString);
                String answer = chatGptApiForBackEndCode(proxy,openAiClient,truncatedString);
                System.out.println(answer);
                System.out.println(extractAndParseDictionary(answer));
                properties.setBackend(extractAndParseDictionary(answer));
            }catch (Exception e){
                e.printStackTrace();
            }


            //启动方式
            File pom = findPomXmlFile(projectFolder.getAbsolutePath());
            if (pom!=null){
                String pom_content = readFileContent2(pom.getAbsolutePath());
                List<String> start_method = new ArrayList<>();
                int batchSize2 = 2000;
                // 计算总批数
                int totalBatches2 = (int) Math.ceil((double) pom_content.length() / batchSize2);
                // 循环处理每个批次
                for (int batchNumber = 0; batchNumber < totalBatches2; batchNumber++) {
                    // 计算当前批次的起始和结束索引
                    int startIndex2 = batchNumber * batchSize2;
                    int endIndex2 = Math.min((batchNumber + 1) * batchSize2, pom_content.length());

                    // 获取当前批次的数据子字符串
                    String currentBatch2 = pom_content.substring(startIndex2, endIndex2);

                    // 处理当前批次的数据
                    String start_result = chatGptApiForStartMethod(proxy,openAiClient,currentBatch2);

                    for (String method:parseArray(start_result)){
                        start_method.add(method);
                    }
                }
                properties.setStartMethod(duplicateListBySet(start_method));
                System.out.println(duplicateListBySet(start_method));
            }
            System.out.println(properties.toString());
            writeToTextFile(properties.toString()+"\n","D:\\Desktop\\RQ2-Baseline0.txt");
        }


    }

}
