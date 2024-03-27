
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ruleDatabase {

    public static List<String> Ebstract(List<String> dir,List<String> characteristic){

        List<String> describe = new ArrayList<>(dir);
        List<String> mainFrontFile = new ArrayList<>();
        for(String file:describe){
            if(file.endsWith(".jsp")||file.endsWith(".js")||file.endsWith(".html")||file.endsWith(".css")){
                mainFrontFile.add(file);
            }
        }
        describe.retainAll(characteristic);
        describe.addAll(mainFrontFile);
        return describe;
    }


    public static List<String> backSearch(List<String> describe){
        List<String> result = new ArrayList<>();
        List<String> dataBasechara = Arrays.asList("oracle","mysql","microsoft sql server","postgresql","mongodb","redis"
                ,"ibm db2","elasticsearch","sqlite","microsoft access","snowflake","cassandra","mariadb","splunk","amazon dynamodb"
                ,"microsoft azure sql database","hive","databricks","teradata","google bigquery","filemaker","neo4j","sap hana"
                ,"solr","sap adaptive server","hbase","microsoft azure cosmos db","postgis","influxdb","firebird","couchbase"
                ,"microsoft azure synapse analytics","memcached","informix","spark sql","amazon redshift","impala"
                ,"firebase realtime database","couchdb","vertica","netezza","dbase","presto","clickhouse","apache flink"
                ,"opensearch","google cloud firestore","greenplum","amazon aurora","oracle essbase","etcd","h2","marklogic"
                ,"realm","hazelcast","kdb","algolia","prometheus","datastax enterprise","jackrabbit","derby","ehcache"
                ,"microsoft azure search","cockroachdb","sphinx","singlestore","aerospike","graphite","google cloud datastore"
                ,"interbase","scylladb","microsoft azure data explorer","trino","riak kv","ingres","apache jena - tdb"
                ,"sap sql anywhere","microsoft azure table storage","adabas","virtuoso","accumulo","hypersql","ignite"
                ,"google cloud bigtable","arangodb","timescaledb","sap iq","orientdb","openedge","ravendb","google cloud spanner"
                ,"rocksdb","ibm cloudant","unidata,universe","gemfire","rethinkdb","yugabytedb","infinispan","maxdb","oracle nosql"
                ,"oracle berkeley db","edb postgres","rrdtool","leveldb","pouchdb","dolphindb","percona server for mysql","intersystems iris"
                ,"lmdb","heavy.ai","intersystems caché","tidb","geode","cloudkit","ims","apache druid","citus","apache phoenix"
                ,"4d","sap advantage database server","apache drill","tdengine","amazon simpledb","duckdb","amazon neptune","oceanbase"
                ,"oracle coherence","exasol","janusgraph","memgraph","nebulagraph","amazon cloudsearch","monetdb","jbase","opentsdb"
                ,"spatialite","graphdb","coveo","gridgain","voltdb","tibero","griddb","db4o","datomic","timesten","questdb","basex"
                ,"empress","objectstore","sqlbase","amazon documentdb","mnesia","ibm db2 warehouse","tigergraph","cubrid","firebolt",
                "tarantool","nonstop sql","stardog","actian nosql database","fauna","litedb","dgraph","datameer","matisse","oracle rdb"
                ,"objectbox","pinecone","dataease","matrixone","altibase","apache kylin","gt.m","gigaspaces","zodb","foundationdb"
                ,"giraph","eventstoredb","apache hawq","msql","nuodb","infobright","opengauss","xapian","perst","model 204","sql.js"
                ,"allegrograph","ncache","sedna","dolt","hibari","northgate reality","dbisam","typedb","hpe ezmeral data fabric"
                ,"soliddb","idms","rdf4j","planetscale","1010data","hfsql","gbase","d3","tdsql for mysql","actian vector","amazon timestream"
                ,"websphere extreme scale","meilisearch","openbase","geomesa","victoriametrics","kognitio","m3db","scidb","blazegraph"
                ,"exist-db","bigchaindb","vitess","frontbase","nexusdb","objectdb","milvus","yellowbrick","kairosdb","rockset","mapdb"
                ,"openinsight","graph engine","cratedb","scalearc","boltdb","r:base","starrocks","sqream db","amazon keyspaces","atoti"
                ,"splice machine","scalaris","strabon","vistadb","postgres-xl","extremedb","datacom/db","4store","weaviate","starcounter"
                ,"alasql","modeshape","keydb","searchblox","lokijs","kinetica","ittia","surrealdb","lovefield","objectivity/db"
                ,"apache iotdb","infinitegraph","redland","trafodion","rasdaman","kingbase","apache doris","sequoiadb","typesense"
                ,"transbase","raima database manager","databend","apache pinot","openqm","gemstone/s","jade","percona server for mongodb"
                ,"project voldemort","elassandra","alibaba cloud maxcompute","hypergraphdb","ejdb","actian fastobjects","tajo"
                ,"alibaba cloud analyticdb for mysql","heroic","harperdb","kyligence enterprise","tokyo tyrant","flockdb","fluree"
                ,"brytlyt","mulgara","comdb2","cubicweb","mimer sql","pipelinedb","axibase","redstore","hugegraph","graphbase"
                ,"featurebase","translattice","neventstore","ydb","alibaba cloud analyticdb for postgresql","anzograph db","qdrant"
                ,"elevatedb","elliptics","marqo","speedb","alibaba cloud log service","dydra","alibaba cloud apsaradb for polardb"
                ,"riak ts","rdfox","stsdb","geospock","leanxcale","cnosdb","exorbyte","machbase","tinkergraph","cloudflare workers kv"
                ,"actian psql","valentina server","bangdb","manticore search","irondb","xtremedata","ultipa","brightstardb","immudb"
                ,"arcadedb","upscaledb","terminusdb","kyoto tycoon","sparksee","siaqodb","quasardb","eloquera","tigris","bigobject"
                ,"tomp2p","hyperleveldb","h2gis","linter","blueflood","senseidb","alibaba cloud table store","fujitsu enterprise postgres"
                ,"tibco computedb","raptordb","acebase","skytable","sitewhere","ledisdb","whitedb","antdb","terarkdb","torodb"
                ,"hawkular metrics","jethrodata","velocitydb","xtdb","edge intelligence","nosdb","faircom db","transwarp kundb"
                ,"greptimedb","actordb","origodb","ibm db2 event store","iboxdb","esgyndb","alibaba cloud tsdb","dragonfly","siridb"
                ,"badger","nsdb","indica","infinitydb","warp 10","oushudb","datafs","cachelot.io","pieclouddb","agensgraph","wakandadb"
                ,"galaxybase","scaleout stateserver","faircom edge","smallsql","newts","sparkledb","resin cache","swc-db","jaguardb"
                ,"swaydb","bergdb","cortexdb","covenantsql","daggerdb","edgelessdb","helium","hgraphdb","jasdb","k-db","rizhiyi"
                ,"sadas engine","searchxml","spacetime","tkrzw","transwarp argodb","transwarp stellardb","yaacomo","yanza");

        for(String database:describe){
            if (dataBasechara.contains(database)){
                result.add(database);
            }
        }
        return result;
    }

    public static List<String> frontSearch(List<String> describe){//如何减少计算复杂度
        List<String> result = new ArrayList<>();

        if(describe.contains("package.json")){//r1
            System.out.println("have frame");
            result.add("have frame");
            if(describe.contains("App.vue")&&describe.contains("main.js")){//r2
                System.out.println("vue");
                result.add("vue");
            }
            if(describe.contains("App.js")&&describe.contains("index.js")){//r3r4
                System.out.println("react or preact");
                result.add("react or preact");
            }
            if(describe.contains("App.svelte")||describe.contains("Button.svelte")){//r5
                System.out.println("svelte");
                result.add("svelte");
            }
            if(describe.contains("angular.json")&&describe.contains("main.ts")&&describe.contains("app.component.ts")){//r6
                System.out.println("angular");
                result.add("angular");
            }
            if(describe.contains(".ember-cli")||describe.contains("ember-cli-build.js")){//r7
                System.out.println("ember");
                result.add("angular");
            }
            if (describe.contains("lit.js")){//r8
                System.out.println("lit");
                result.add("lit");
            }
        }
        if (describe.contains("echarts.js")||describe.contains("echarts.min.js")||describe.contains("echarts")){//r9
            System.out.println("echarts");
            result.add("echarts");
        }
        if(describe.contains("jquery.js")||describe.contains("jquery.min.js")){//r10
            System.out.println("jquery");
            result.add("jquery");
            if (describe.contains("bootstrap.js")){//r11
                System.out.println("bootstrap");
                result.add("bootstrap");
            }
        }
        if (describe.contains("element-ui")){//r12
            System.out.println("element-ui");
            result.add("element-ui");
        }
        if (describe.contains("antd")){//r13
            System.out.println("antd");
            result.add("antd");
        }
        if (describe.contains("vuetify")){//r14
            System.out.println("vuetify");
            result.add("vuetify");
        }
        if (describe.contains("layui")){//r15
            System.out.println("layui");
            result.add("layui");
        }
        if (describe.contains("semantic.min.js")||describe.contains("semantic.js")){//r16
            System.out.println("semantic-ui");
            result.add("semantic-ui");
        }
        if (describe.contains("yarn.lock")){//r17
            System.out.println("use yarn to download");
            result.add("use yarn to download");
        }

        if (result.isEmpty()){
            for (String s:describe){
                if (s.endsWith(".jsp")){
                    System.out.println("jsp");
                    result.add("jsp");
                    break;
                }
            }

        }


        return result;

    }

    public static List<String> duplicateListBySet(List<String> list) {//去重
        HashSet h = new HashSet(list);
        List newList = new ArrayList();
        newList.addAll(h);
        return newList;
    }


    public static boolean isSpringBoot(List<String>files){
        for (String s:files){
            if (s.contains("spring_boot")){
                return true;                                                                   
            }
        }
        return false;
    }


    /**
     * 得到文件名称，不包括目录名称但是有外部库名
     *
     * @param file      文件
     * @param fileNames 文件名
     * @return {@link List}<{@link String}>
     */
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
                    if (file.isFile() && !isIgnoredDirectory(file) && endsWithExtensions(file.getName(), extensions)) {
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



    public static void main(String[] args) {
        List<String> characteristic = new ArrayList<>();
        characteristic = Arrays.asList("App.vue","main.js","package.json","node_modules","App.js","i18n","index.js","angular.json","tsconfig.app.json","main.ts"
                ,"app.component,ts","tsconfig.spec.json","karma.conf.js","App.svelte","Button.svelte","rollup.config.js","environment.js",".ember-cli",".jshintrc"
                ,"ember-cli-build.js","index.html","resolver.js","router.js","package-lock.json","preact","lit.js","yarn.lock","bootstrap.js","jquery.js"
                ,"jquery.min.js","semantic.min.js","element-ui","antd","vuetity","layui.js","echarts","echarts.js","echarts.min.js");


//        List<String> describe = new ArrayList<>();
//        File files = new File("D:\\Desktop\\javaweb\\qinlouyue-master");
//        List<String> fileNames = new ArrayList<>();
//        fileNames = getFileNames(files, fileNames);
//        System.out.println(fileNames);
//        System.out.println(isSpringBoot(fileNames));
//        describe = Ebstract(fileNames,characteristic);
//        System.out.println(describe);
//        List<String> result = new ArrayList<>();
//        result = frontSearch(describe);
//        System.out.println(result);

        File projectFolder = new File("D:\\Desktop\\javaweb\\bicycleSharingServer-master");

        String[] extensions = {".xml", ".properties", ".yaml", ".yml"};
        List<File> files1 = findFilesWithExtensions(projectFolder, extensions);
        System.out.println(files1.toString());

//        for (File file : files1) {
//            System.out.println(file.getAbsolutePath());
//        }

    }
}
