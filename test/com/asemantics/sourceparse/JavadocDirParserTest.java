package com.asemantics.sourceparse;

import com.asemantics.sourceparse.ObjectsTable;
import com.asemantics.sourceparse.DirectoryParser;
import com.asemantics.storage.JenaCoderFactory;
import com.asemantics.storage.JenaCodeModel;
import com.asemantics.storage.JenaCodeStorage;
import com.asemantics.model.CodeHandler;
import com.asemantics.model.CodeModelException;
import com.asemantics.model.CodeStorage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Unit test of JavadocSourceDirParser.
 */
public class JavadocDirParserTest extends TestCase {

    ObjectsTable ot;
    JenaCoderFactory jcf;
    JenaCodeModel jcm;
    JenaCodeStorage jcs;
    CodeHandler ch;

    public void setUp() {
        ot   = new ObjectsTable();
        jcf = new JenaCoderFactory();
        jcm  = (JenaCodeModel) jcf.createCodeModel();
        jcs  = jcf.createCodeStorage();
        ch   = jcf.createHandlerOnModel(jcm);
        ch.startParsing("jdk_1.5.0", "java_home");
    }

     public void tearDown() {
        ch.endParsing();
        ot.clear();
        ot   = null;
        jcf = null;
        jcm  = null;
        ch   = null;
    }

    public void testParse() throws IOException, CodeModelException {
        DirectoryParser jsdp = new DirectoryParser( new JavadocFileParser() );
        Statistics statistics = new Statistics();
        CodeHandler sch = statistics.createStatisticsCodeHandler(ch);
        jsdp.initialize(sch, ot);
        jsdp.parseDirectory(new File("../src") );
        jsdp.dispose();
        Map<String,String> params = new HashMap();
        params.put(CodeStorage.FS_FILENAME, "out/src.xml");
        jcs.saveModel(jcm, params);
        System.out.println(statistics);
    }

}