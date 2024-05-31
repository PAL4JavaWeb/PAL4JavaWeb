# PAL4JavaWeb

## Introduction
PAL4JavaWeb is a lightweight approach that utilises static program analysis and prompt engineering to leverage LLM’s (Large Language Models) knowledge for generating setup steps. This project is implemented in Java language and is intended to be run in IntelliJ IDEA.

## Usage
1. Clone this project and the project you want to generate setup steps for.
2. Locate **PAL4JavaWeb\src\main\java\chatGpt.java**, which serves as the main file for generating the setup steps.
3. Modify the **projname** in the step2 file to the address of your target project on your PC.
4. Change the Apikey of chatGpt to yours.
5. Modify the arguments in the `writeToText()` function to the location you desire.
6. The result will be automatically generated at the address specified in step 5.
7. Run the `chatGpt.java`.

## Call Graph
Repository: [java-all-call-graph](https://github.com/Adrninistrator/java-all-call-graph.git)

1. Package the project into a .war or .jar file.
2. Locate the **_jacg_config/config.properties** file.
3. Modify the `app.name` to your preference.
4. Find the **_jacg_config/jar_dir.properties** file.
5. Specify the location of the war/jar file.
6. Find the **_jacg_config/config_db.properties** file.
7. Adjust the database configuration (url, username, password).
8. Find the **_jacg_config/method_class_4caller** file.
9. Specify the class name you wish to analyse.
10. Run `TestRunnerWriteDb.java` and `TestRunnerGenAllGraph4Caller.java`. The result will be automatically generated in the "_jacg-[time]" directory.

## Baseline1
1. Locate the **PAL4JavaWeb\src\main\java\KeywordExtract.java** file.
2. Modify the `directoryPath` to the directory of your baseline projects.
3. Modify the arguments in the `writeToText()` function to the desired location.
4. The result will be automatically generated at the address specified in step 3.

## Baseline2
1. Locate the **PAL4JavaWeb\src\main\java\RQ2Baseline1.java** file.
2. Modify the `directoryPath` to the directory of your baseline projects.
3. Modify the arguments in the `writeToText()` function to the desired location.
4. The result will be automatically generated at the address specified in step 3.

## Test Applications
Located in the `javaweb` directory.

## Example of the Generation Module
<img src="https://github.com/PAL4JavaWeb/PAL4JavaWeb/assets/76843234/62fe7902-db25-4e8e-b56f-c79bd699ef25" alt="generate" width="350" height="800"/>




