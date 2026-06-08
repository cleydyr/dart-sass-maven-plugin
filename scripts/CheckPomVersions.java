/*
  This script checks the versions of the pom.xml files in the repository.
  There are two test pom files in this repo: src/test/resources/musl/test-project/pom.xml and
  src/test/resources/test-project/pom.xml. We want to ensure they use the same version as the plugin version set in the
  root pom.xml. This script automatically checks if that's the case.
 */

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

public class CheckPomVersions {
    public static void main(String[] args) {
        String pluginVersion = getPluginVersion("pom.xml", "/project/version");

        String testProjectUsedVersion = getPluginVersion("src/test/resources/test-project/pom.xml", "/project/build/plugins/plugin[artifactId='dart-sass-maven-plugin']/version");
        String muslTestProjectUsedVersion = getPluginVersion("src/test/resources/musl/test-project/pom.xml", "/project/build/plugins/plugin[artifactId='dart-sass-maven-plugin']/version");

        assert testProjectUsedVersion != null;
        assert muslTestProjectUsedVersion != null;
        assert pluginVersion != null;

        assert pluginVersion.equals(testProjectUsedVersion) : String.format("Plugin version in test-project pom.xml (%s) does not match the root plugin version (%s).", testProjectUsedVersion, pluginVersion);
        assert pluginVersion.equals(muslTestProjectUsedVersion) : String.format("Plugin version in musl test-project pom.xml (%s) does not match the root plugin version (%s).", muslTestProjectUsedVersion, pluginVersion);

    }

    private static String getPluginVersion(String fileName, String xpathExpression) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            File pomFile = new File(fileName);

            Document doc = builder.parse(pomFile);

            XPathFactory xPathfactory = XPathFactory.newInstance();

            XPath xpath = xPathfactory.newXPath();

            XPathExpression expr = xpath.compile(xpathExpression);

            return (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (IOException e) {
            return null;
        } catch (XPathExpressionException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
