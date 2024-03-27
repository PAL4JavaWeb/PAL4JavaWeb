import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonPointer;
import com.sun.deploy.panel.TreeRenderers;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class readTxt {
    public static String paths=
            "D:\\Desktop\\javaweb\\Java-BookStoreShoppingCart-master\\_jacg_o_er\\20230228-165942.397\\CartController@doPost@Q4Xunn7juwjNwXrGJ0xXug==#073.txt";
    public static String methodcall_path =
            "D:\\Desktop\\javaweb\\Java-BookStoreShoppingCart-master\\out\\artifacts\\bookstore_war2\\bookstore_war.war-output_javacg\\method_call.txt";

    public static List<Tree> readTxts(String path) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "gbk");
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        int count = 0;
        List<Tree> list = new ArrayList<Tree>();
        List<Tree> methodList = readFileContent(methodcall_path);
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

    public static List readFileContent(String fileName) {
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


    public static class ReplacementTree {
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
        private Tree buildChilTree(Tree tree,List<Tree> list,int level) {
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

    public static properties traceTreeForProperties(List<Tree> list, properties p){//遍历
        for(Tree t:list){
            if(t.getChildren().size()>0){
                p=traceTreeForProperties(t.getChildren(), p);
            }
            if(t.getParam()!=null){
                System.out.println(t.getParam());
                List<String> database = new ArrayList<>();
                for(String s:t.getParam()){
                    if(isDataBase(s)){
                        database.add(s);
                        p.setDataBase(database);
                    }
                    if(isFrontFile(s)){
                        String frontFrame = findFrontFrame(s);
                        p.setFrontFrame(frontFrame);
                    }
                }
            }
        }
        return p;
    }


    private static boolean isDataBase(String s){
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
        for(String database:dataBasechara){
            if(s.toLowerCase().contains(database)){
                return true;
            }

        }

        return false;
    }

    private static boolean isFrontFile(String s){
        if(s.toLowerCase().contains(".jsp")){
            return true;
        }
        return false;
    }

    public static List<String> findSqlFile(){
        File files = new File("D:\\Desktop\\javaweb\\Java-BookStoreShoppingCart-master\\");
        String[] names = files.list();
        List<String> sqlFiles = new ArrayList<>();
        for(String s:names){
            boolean a = s.toLowerCase().contains(".sql")||s.toLowerCase().contains(".db");
            if(a){
                sqlFiles.add(s);
            }
        }
        return sqlFiles;
    }

    public static String findFrontFrame(String s1){
        File files = new File("D:\\Desktop\\javaweb\\Java-BookStoreShoppingCart-master\\");
        String[] names = files.list();
        for(String s:names){
            boolean a = s.toLowerCase().contains("vue");
            boolean b = s.toLowerCase().contains("bootstrap");
            boolean c = s.toLowerCase().contains("react");
            if(a){
                return "vue框架";
            }
            else if(b){
                return "bootstrap框架";
            }
            else if(c){
                return "react框架";
            }
            else if(s1.toLowerCase().contains("jsp")){
                return "jsp技术";
            }
        }
        return "无";
    }

    public static String findStartMethod(){
        File files = new File("D:\\Desktop\\javaweb\\Java-BookStoreShoppingCart-master\\");
        String[] names = files.list();
        for(String s:names){
            boolean a = s.toLowerCase().contains("application");
            if(a){
                return "执行main函数";
            }
        }
        return "放入tomcat执行";
    }

    public static void generateResult(properties p){
        String startMethod = findStartMethod();
        p.setStartMethod(startMethod);
        List<String> sqlFile = findSqlFile();
        p.setSqlFile(sqlFile);
        System.out.println("1.通过git下载源码");
        System.out.println("2.下载数据库"+p.getDataBase().get(0)+",执行"+p.getSqlFile().get(0)+",初始化数据");
        System.out.println("3.前端框架是"+p.getFrontFrame()+",请进行相应配置");
        System.out.println("4.项目启动方式为"+p.getStartMethod());
        System.out.println("5.运行后,项目访问路径:http://localhost:8080");
        System.out.println("6.账号密码:admin/admin");
    }


    private static class properties{
        public List<String> getDataBase() {
            return dataBase;
        }

        public void setDataBase(List<String> dataBase) { this.dataBase = dataBase; }

        public String getFrontFrame() {
            return frontFrame;
        }

        public void setFrontFrame(String frontFrame) {
            this.frontFrame = frontFrame;
        }

        public List<String> getSqlFile() {
            return sqlFile;
        }

        public void setSqlFile(List<String> sqlFile) {
            this.sqlFile = sqlFile;
        }

        public String getStartMethod() {
            return startMethod;
        }

        public void setStartMethod(String startMethod) {
            this.startMethod = startMethod;
        }

        private List<String> dataBase;
        private String frontFrame;
        private List<String> sqlFile;
        private String startMethod;
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
                    list2 = new ReplacementTree().builTree(list2);
                    list1.addAll(list2);
                }
            }
        }

        return list1;
    }

    public static void main(String[] args) throws IOException {
//        List list = readFileContent(methodcall_path);
//        String jsonString = JSON.toJSONString(list);
//        System.out.println(jsonString);


        List<Tree> list1 = new ArrayList<>();
        list1 = buildTreeFromFolder("D:\\Desktop\\javaweb\\Java-BookStoreShoppingCart-master\\_jacg_o_er\\20230228-165942.397\\");
        List<List<String>> paramList = new ArrayList<>();
        getParams(list1, paramList);
        System.out.println(paramList);
//        list1 = readTxts("D:\\Desktop\\javaweb\\Java-BookStoreShoppingCart-master\\_jacg_o_er\\20230228-165942.397\\CartController@doPost@Q4Xunn7juwjNwXrGJ0xXug==#073.txt");
//        list1 = new ReplacementTree().builTree(list1);
//
//        List<Tree> list2 = new ArrayList<>();
//        list2 = readTxts("D:\\Desktop\\javaweb\\Java-BookStoreShoppingCart-master\\_jacg_o_er\\20230228-165942.397\\CartController@init@bHnmtLV7JIrQl41ZduS-2g==#025.txt");
//        list2 = new ReplacementTree().builTree(list2);
//
//        list1.addAll(list2);

//        String jsonString = JSON.toJSONString(list);
//        System.out.println(jsonString);
//        String s = new String();
//        s="{"+traceTree(list,s).replaceAll("(\\[\\[\")|(\"\\]\\])|(\\[\")|(\"\\])","")+"}";
//
//        System.out.println(s);

//        properties p = new properties();
//        p = traceTreeForProperties(list1,p);
//
//        generateResult(p);



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


}
