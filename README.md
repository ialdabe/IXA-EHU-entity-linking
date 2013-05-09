IXA-EHU-entity-linking
======================

This repository takes as input a file in KAF format where the entities are tagged. It returns a KAF formal file, where the entities are disambiguated and liked to dbpedia. 

### 1 Requirements

Maven 3
Scala 2.9
Java 1.7

It is necessary to have installed a modified version of the dbpedia-spotlight. To do so, go to: https://github.com/ialdabe/IXA-EHU-DBpedia-spotlight and follow the instructions. 

Once the dbpedia-spotlight is installed, continue to step 2

### 2 Download IXA-EHU-entity-linking

git clone git@github.com:ialdabe/IXA-EHU-entity-linking.git
OR
git clone  git://github.com/ialdabe/IXA-EHU-entity-linking.git

The command will create a directory called IXA-EHU-entity-linking. This repository contains all the necessary information to install the repository. 

### 3 Installation

Before installing IXA-EHU-entity-linking, it is necessary to change the pom.xml file

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <internal.path>/sc04a2/users/jibalari/entitylinking/</internal.path>
  </properties>

The internal.path has to point out to the directory where the dbpedia-spotlight-0.6-jar-with-dependencies.jar file is located (previously installed, see step 1)

Once the path is correctly set, install the repository:

mvn clean package

### 4 Test the repository 

cat ner.kaf | java -jar target/IXA-EHU-entity-linking-1.0-SNAPSHOT.jar

The input file (ner.kaf) is a file in KAF format where the entities are tagged. The IXA-EHU-entity-linking-1.0-SNAPSHOT.jar takes it, desambiguates the given entities with the dbpedia spotlight and then it adds the disambiguation to the KAF file. The output is a KAF file shown in the standard output. 

So far, the program works with two languages: English and Spanish. The program verifies the language of the file by looking at the "lang" tag of the input XML file. Thus, it is necessary to correctly set this tag for a correct execution of the program. 
