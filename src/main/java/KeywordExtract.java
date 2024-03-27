import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KeywordExtract {
    public static void main(String[] args) throws IOException {

        String directoryPath = "D:\\Desktop\\javaweb";

        List<String> fileNames = listFiles(directoryPath);

        List<String> frontendFrameworks = Arrays.asList(
                "Angular",
                "React",
                "Vue.js",
                "jQuery",
                "Bootstrap",
                "Svelte",
                "Ember.js",
                "Backbone.js",
                "Polymer",
                "D3.js",
                "Three.js",
                "Chart.js",
                "GSAP (GreenSock Animation Platform)",
                "Redux",
                "MobX",
                "RxJS",
                "Express.js",
                "Next.js",
                "Nuxt.js",
                "Gatsby",
                "Webpack",
                "Babel",
                "TypeScript",
                "Flow",
                "GraphQL",
                "Apollo Client",
                "Relay",
                "Axios",
                "Fetch API",
                "Sass",
                "Less",
                "Stylus",
                "PostCSS",
                "CSS-in-JS",
                "CSS Grid",
                "Flexbox",
                "Tailwind CSS",
                "Material-UI",
                "Ant Design",
                "Bulma",
                "Foundation",
                "Semantic UI",
                "Ionic",
                "Electron",
                "Progressive Web Apps (PWA)",
                "Service Workers",
                "IndexedDB",
                "Local Storage",
                "WebSockets",
                "WebRTC",
                "Firebase",
                "AWS Amplify",
                "Content Delivery Network (CDN)",
                "Docker",
                "Kubernetes",
                "Jest",
                "Mocha",
                "Cypress",
                "Selenium",
                "Puppeteer",
                "Lighthouse",
                "ESLint",
                "Prettier",
                "Husky",
                "Parcel",
                "Rollup",
                "Snowpack",
                "Vite",
                "Jest",
                "Enzyme",
                "Jasmine",
                "Chai",
                "Karma",
                "Puppeteer",
                "Playwright",
                "Yarn",
                "npm",
                "Bower",
                "Grunt",
                "Gulp",
                "WebAssembly (Wasm)",
                "Blazor",
                "ReasonML",
                "Elm",
                "ClojureScript",
                "Flutter",
                "React Native",
                "NativeScript",
                "Cordova",
                "PhoneGap",
                "Apache Cordova",
                "Capacitor",
                "Ionic",
                "Electron",
                "Parcel",
                "Storybook",
                "Bit",
                "Styleguidist",
                "Webpack Bundle Analyzer",
                "Lerna"
                // 其他前端框架和库
        );

        List<String>backendFrameworks = Arrays.asList(
                "SpringBoot",
                "springmvc",
                "spring",
                "thymeleaf",
                "MyBatis",
                "Express",
                "Ruby on Rails",
                "Django",
                "Laravel",
                "ASP.NET",
                "Flask",
                "Play Framework",
                "Phoenix",
                "Koa",
                "NestJS",
                "Gin",
                "FastAPI",
                "Sinatra"
                // 其他后端框架和技术
        );

        List<String> database = Arrays.asList("oracle", "mysql", "microsoft sql server", "postgresql", "mongodb", "redis"
                , "ibm db2", "elasticsearch", "sqlite", "microsoft access", "snowflake", "cassandra", "mariadb", "splunk", "amazon dynamodb"
                , "microsoft azure sql database", "hive", "databricks", "teradata", "google bigquery", "filemaker", "neo4j", "sap hana"
                , "solr", "sap adaptive server", "hbase", "microsoft azure cosmos db", "postgis", "influxdb", "firebird", "couchbase"
                , "microsoft azure synapse analytics", "memcached", "informix", "spark sql", "amazon redshift", "impala"
                , "firebase realtime database", "couchdb", "vertica", "netezza", "dbase", "presto", "clickhouse", "apache flink"
                , "opensearch", "google cloud firestore", "greenplum", "amazon aurora", "oracle essbase", "etcd", "h2", "marklogic"
                , "realm", "hazelcast", "kdb", "algolia", "prometheus", "datastax enterprise", "jackrabbit", "derby", "ehcache"
                , "microsoft azure search", "cockroachdb", "sphinx", "singlestore", "aerospike", "graphite", "google cloud datastore"
                , "interbase", "scylladb", "microsoft azure data explorer", "trino", "riak kv", "ingres", "apache jena - tdb"
                , "sap sql anywhere", "microsoft azure table storage", "adabas", "virtuoso", "accumulo", "hypersql", "ignite"
                , "google cloud bigtable", "arangodb", "timescaledb", "sap iq", "orientdb", "openedge", "ravendb", "google cloud spanner"
                , "rocksdb", "ibm cloudant", "unidata,universe", "gemfire", "rethinkdb", "yugabytedb", "infinispan", "maxdb", "oracle nosql"
                , "oracle berkeley db", "edb postgres", "rrdtool", "leveldb", "pouchdb", "dolphindb", "percona server for mysql", "intersystems iris"
                , "lmdb", "heavy.ai", "intersystems caché", "tidb", "geode", "cloudkit", "ims", "apache druid", "citus", "apache phoenix"
                , "4d", "sap advantage database server", "apache drill", "tdengine", "amazon simpledb", "duckdb", "amazon neptune", "oceanbase"
                , "oracle coherence", "exasol", "janusgraph", "memgraph", "nebulagraph", "amazon cloudsearch", "monetdb", "jbase", "opentsdb"
                , "spatialite", "graphdb", "coveo", "gridgain", "voltdb", "tibero", "griddb", "db4o", "datomic", "timesten", "questdb", "basex"
                , "empress", "objectstore", "sqlbase", "amazon documentdb", "mnesia", "ibm db2 warehouse", "tigergraph", "cubrid", "firebolt",
                "tarantool", "nonstop sql", "stardog", "actian nosql database", "fauna", "litedb", "dgraph", "datameer", "matisse", "oracle rdb"
                , "objectbox", "pinecone", "dataease", "matrixone", "altibase", "apache kylin", "gt.m", "gigaspaces", "zodb", "foundationdb"
                , "giraph", "eventstoredb", "apache hawq", "msql", "nuodb", "infobright", "opengauss", "xapian", "perst", "model 204", "sql.js"
                , "allegrograph", "ncache", "sedna", "dolt", "hibari", "northgate reality", "dbisam", "typedb", "hpe ezmeral data fabric"
                , "soliddb", "idms", "rdf4j", "planetscale", "1010data", "hfsql", "gbase", "d3", "tdsql for mysql", "actian vector", "amazon timestream"
                , "websphere extreme scale", "meilisearch", "openbase", "geomesa", "victoriametrics", "kognitio", "m3db", "scidb", "blazegraph"
                , "exist-db", "bigchaindb", "vitess", "frontbase", "nexusdb", "objectdb", "milvus", "yellowbrick", "kairosdb", "rockset", "mapdb"
                , "openinsight", "graph engine", "cratedb", "scalearc", "boltdb", "r:base", "starrocks", "sqream db", "amazon keyspaces", "atoti"
                , "splice machine", "scalaris", "strabon", "vistadb", "postgres-xl", "extremedb", "datacom/db", "4store", "weaviate", "starcounter"
                , "alasql", "modeshape", "keydb", "searchblox", "lokijs", "kinetica", "ittia", "surrealdb", "lovefield", "objectivity/db"
                , "apache iotdb", "infinitegraph", "redland", "trafodion", "rasdaman", "kingbase", "apache doris", "sequoiadb", "typesense"
                , "transbase", "raima database manager", "databend", "apache pinot", "openqm", "gemstone/s", "jade", "percona server for mongodb"
                , "project voldemort", "elassandra", "alibaba cloud maxcompute", "hypergraphdb", "ejdb", "actian fastobjects", "tajo"
                , "alibaba cloud analyticdb for mysql", "heroic", "harperdb", "kyligence enterprise", "tokyo tyrant", "flockdb", "fluree"
                , "brytlyt", "mulgara", "comdb2", "cubicweb", "mimer sql", "pipelinedb", "axibase", "redstore", "hugegraph", "graphbase"
                , "featurebase", "translattice", "neventstore", "ydb", "alibaba cloud analyticdb for postgresql", "anzograph db", "qdrant"
                , "elevatedb", "elliptics", "marqo", "speedb", "alibaba cloud log service", "dydra", "alibaba cloud apsaradb for polardb"
                , "riak ts", "rdfox", "stsdb", "geospock", "leanxcale", "cnosdb", "exorbyte", "machbase", "tinkergraph", "cloudflare workers kv"
                , "actian psql", "valentina server", "bangdb", "manticore search", "irondb", "xtremedata", "ultipa", "brightstardb", "immudb"
                , "arcadedb", "upscaledb", "terminusdb", "kyoto tycoon", "sparksee", "siaqodb", "quasardb", "eloquera", "tigris", "bigobject"
                , "tomp2p", "hyperleveldb", "h2gis", "linter", "blueflood", "senseidb", "alibaba cloud table store", "fujitsu enterprise postgres"
                , "tibco computedb", "raptordb", "acebase", "skytable", "sitewhere", "ledisdb", "whitedb", "antdb", "terarkdb", "torodb"
                , "hawkular metrics", "jethrodata", "velocitydb", "xtdb", "edge intelligence", "nosdb", "faircom db", "transwarp kundb"
                , "greptimedb", "actordb", "origodb", "ibm db2 event store", "iboxdb", "esgyndb", "alibaba cloud tsdb", "dragonfly", "siridb"
                , "badger", "nsdb", "indica", "infinitydb", "warp 10", "oushudb", "datafs", "cachelot.io", "pieclouddb", "agensgraph", "wakandadb"
                , "galaxybase", "scaleout stateserver", "faircom edge", "smallsql", "newts", "sparkledb", "resin cache", "swc-db", "jaguardb"
                , "swaydb", "bergdb", "cortexdb", "covenantsql", "daggerdb", "edgelessdb", "helium", "hgraphdb", "jasdb", "k-db", "rizhiyi"
                , "sadas engine", "searchxml", "spacetime", "tkrzw", "transwarp argodb", "transwarp stellardb", "yaacomo", "yanza"
                // 数据库
        );

        ArrayList<String> FF= new ArrayList<String>(frontendFrameworks);
        ArrayList<String> BF= new ArrayList<String>(backendFrameworks);
        ArrayList<String> DB= new ArrayList<String>(database);

        // 打印文件名
        for (String fileName : fileNames) {
            System.out.println("开始处理"+fileName);
            writeToTextFile(fileName+":","D:\\Desktop\\RQ1-Baseline2.txt");
            // 项目文件夹的路径
            String projectFolderPath = "D:\\Desktop\\javaweb\\"+fileName;
                    Set<String> frontEndConfig = searchKeywordsInProject(projectFolderPath, FF);
        Set<String> backEndConfig = new HashSet<>();
        Set<String> dataBase = searchKeywordsInProject(projectFolderPath, DB);
        Set<String> url = new HashSet<>();
        Set<String> username = new HashSet<>();
        Set<String> password = new HashSet<>();
        Set<String> serverPort = new HashSet<>();
        Set<String> backEndFramework = searchKeywordsInProject(projectFolderPath, BF);
        Set<String> sqlFile = new HashSet<>();
        Set<String> startMethod = new HashSet<>();


        // 调用搜索方法
        List<CredentialInfo> results = findCredentialsInProject(projectFolderPath);

        // 打印搜索到的内容
        for (CredentialInfo result : results) {

            if(result.getField().equals("url")){
                url.add(result.getValue());
                backEndConfig.add(result.getFileName());
            }
            if(result.getField().equals("username")){
                username.add(result.getValue());
                backEndConfig.add(result.getFileName());
            }
            if(result.getField().equals("password")){
                password.add(result.getValue());
                backEndConfig.add(result.getFileName());
            }
            if(result.getField().equals("server.port")){
                serverPort.add(result.getValue());
                backEndConfig.add(result.getFileName());
            }
            if (result.getField().equals("sql-file")){
                sqlFile.add(result.getFileName());
            }
            if (result.getField().equals("start-method")){
                startMethod.add(result.getValue());
            }
        }

        StringBuilder stringBuilder = new StringBuilder("{\n" +
                "\"front-end\": [");
        for (String element : frontEndConfig){
            stringBuilder.append("\"").append(element).append("\",");
        }
        stringBuilder.append("],\n" +
                "\"back-end-config\":[");

        for (String element : backEndConfig){
            stringBuilder.append("\"").append(element).append("\",");
        }

        stringBuilder.append("],\n" +
                "\"back-end\":{\n\"Database\":[");

        for (String element : dataBase){
            stringBuilder.append("\"").append(element).append("\",");
        }
        stringBuilder.append("],\n" +
                "\"url\":[");
        for (String element : url){
            stringBuilder.append("\"").append(element).append("\",");
        }
        stringBuilder.append("],\n" +
                "\"modified configuration\":{\n\"url\":[");
        for (String element : url){
            stringBuilder.append("\"").append(element).append("\",");
        }
        stringBuilder.append("],\n" +
                "\"username\":[");
        for (String element : username){
            stringBuilder.append("\"").append(element).append("\",");
        }
        stringBuilder.append("],\n" +
                "\"password\":[");
        for (String element : password){
            stringBuilder.append("\"").append(element).append("\",");
        }
        stringBuilder.append("],\n},\n" +
                "\"server-port\":[");
        for (String element : serverPort){
            stringBuilder.append("\"").append(element).append("\",");
        }
        stringBuilder.append("],\n" +
                "\"back-end framework\":[");
        for (String element : backEndFramework){
            stringBuilder.append("\"").append(element).append("\",");
        }
        stringBuilder.append("]\n},\n\"sql-file\":[");
        for (String element : sqlFile){
            stringBuilder.append("\"").append(element).append("\",");
        }
        stringBuilder.append("],\n\"start-method\":[");
        for (String element : startMethod){
            stringBuilder.append("\"").append(element).append("\",");
        }
        stringBuilder.append("],\n}");
        System.out.println(stringBuilder.toString());

//        System.out.println("\n" +
//                "{\n" +
//                "\"front-end\": "+frontEndConfig+",\n" +
//                "\"back-endconfig\":"+backEndConfig+",\n" +
//                "\"back-end\":{\n" +
//                "\"Database\":"+dataBase+",\n" +
//                "\"modified configuration\":{\n" +
//                "\"url\":"+url+",\n" +
//                "\"username\":"+username+",\n"+
//                "\"password\":"+password+",\n"+
//                "},\n" +
//                "\"server-port\":"+serverPort+",\n" +
//                "\"back-end framework\":"+backEndFramework+",\n" +
//                "},\n" +
//                "\"sql-file\":"+sqlFile+",\n" +
//                "\"start-method\":"+startMethod+"\n" +
//                "}\n");

        writeToTextFile(stringBuilder.toString()+"\n","D:\\Desktop\\RQ1-Baseline2.txt");

        }

    }

    public static void writeToTextFile(String content, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(content);
            writer.newLine();  // 换行，可选
        } catch (IOException e) {
            e.printStackTrace();
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

    // 在整个项目文件夹中搜索关键词的方法
    private static Set<String> searchKeywordsInProject(String projectFolderPath, ArrayList<String> keywords) {
        Set<String> foundKeywords = new HashSet<>();
        try {
            Files.walk(Paths.get(projectFolderPath), Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)
                    .filter(path -> Files.isRegularFile(path) && !path.toString().contains(File.separator + "node_modules" + File.separator))
//                    .filter(Files::isRegularFile)
                    .forEach(filePath -> foundKeywords.addAll(searchKeywordsInFile(filePath, keywords)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundKeywords;
    }

    // 在单个文件中搜索关键词的方法
    private static Set<String> searchKeywordsInFile(Path filePath, ArrayList<String> keywords) {
        Set<String> foundKeywordsInReposity = new HashSet<>();
        try {
            String content = new String(Files.readAllBytes(filePath));

            for (String keyword : keywords) {
                String regex = "\\b" + Pattern.quote(keyword) + "\\b";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(content);

                // 使用 Set 存储已找到的关键词，避免重复
                Set<String> foundKeywordsInFile = new HashSet<>();

                while (matcher.find()) {
//                    String foundKeyword = content.substring(matcher.start(), matcher.end());
                    String foundKeyword = matcher.group();
                    foundKeywordsInReposity.add(foundKeyword);
                    // 检查关键词是否已经存在，避免重复输出
//                    if (foundKeywordsInFile.add(foundKeyword)) {
//                        System.out.println("在文件 '" + filePath + "' 中找到关键词 '" + foundKeyword + "'，位置：" +
//                                matcher.start() + " 到 " + matcher.end());
//                    }
                }
            }
//            System.out.println(foundKeywordsInReposity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundKeywordsInReposity;
    }

    private static List<CredentialInfo> findCredentialsInProject(String projectFolderPath) {
        List<CredentialInfo> results = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get(projectFolderPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    // 避开node_modules文件夹
                    if (dir.endsWith("node_modules")) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    findCredentialsInFile(file, results);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    private static void findCredentialsInFile(Path filePath, List<CredentialInfo> results) {
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            // 匹配username、url、password、server-port
            String regex = "(username|password|server.port)[:=]\\s*([\\S]+)";
            String regex2 = "(url)[:=]\\s*(jdbc:mysql://[^\"]+)";
            String regex3 = "SpringApplication.run";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Pattern pattern2 = Pattern.compile(regex2,Pattern.CASE_INSENSITIVE);
            if(filePath.toString().endsWith(".sql")&& !filePath.toString().contains("target\\classes"))
            {
                results.add(new CredentialInfo(filePath.toString(),"sql-file",filePath.toString()));
            }

            for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
                String line = lines.get(lineNumber);
                Matcher matcher = pattern.matcher(line);
                Matcher matcher2 = pattern2.matcher(line);
                if (line.contains(regex3)){
                    results.add(new CredentialInfo(filePath.toString(),"start-method",regex3));
                }
                if (line.contains("<packaging>war</packaging>")){
                    results.add(new CredentialInfo(filePath.toString(),"start-method","打包成war上传tomcat"));
                }

                while (matcher.find()) {
                    String field = matcher.group(1).toLowerCase();
                    String value = matcher.group(2);
                    results.add(new CredentialInfo(filePath.toString(), field, value));
                }

                while (matcher2.find()) {
                    String field = matcher2.group(1).toLowerCase();
                    String value = matcher2.group(2);
                    results.add(new CredentialInfo(filePath.toString(), field, value));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 凭据信息类，包含文件名、字段和值
    static class CredentialInfo {
        private final String fileName;
        private final String field;
        private final String value;

        public CredentialInfo(String fileName, String field, String value) {
            this.fileName = fileName;
            this.field = field;
            this.value = value;
        }

        public String getFileName() {
            return fileName;
        }

        public String getField() {
            return field;
        }

        public String getValue() {
            return value;
        }
    }


}
