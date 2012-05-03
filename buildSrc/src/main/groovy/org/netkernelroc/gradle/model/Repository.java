package org.netkernelroc.gradle.model;

import groovy.util.Node;
import groovy.util.NodeList;
import groovy.util.XmlParser;
import groovy.xml.QName;

import java.util.*;

public class Repository {
  private AppositeStore appositeStore;
  private String baseUrl;
  private String path;
  private String set;
  private Map<String, Package> packages = new HashMap<String, Package>();

  public Repository(AppositeStore appositeStore, String baseUrl, String path, String set) {
    this.appositeStore = appositeStore;
    this.baseUrl = baseUrl;
    this.path = path;
    this.set = set;
    try {
      loadPackages();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadPackages() throws Exception {
    loadPackageForType(VersionType.BASE);
    loadPackageForType(VersionType.UPDATE);
    loadPackageForType(VersionType.SECURITY);
  }

  private void loadPackageForType(VersionType versionType) throws Exception {
    String baseRepositoryUrl = baseUrl + "/netkernel/" + path + "/" + versionType.name().toLowerCase() + "/" + set + "/repository.xml";
    System.out.println("Loading " + baseRepositoryUrl);

    XmlParser parser = new XmlParser();
    Node repositoryNode = parser.parse(baseRepositoryUrl);
    for (Object aPackageObj : repositoryNode.children()) {
      if (aPackageObj instanceof Node) {
        Node aPackage = (Node) aPackageObj;

        Object nameObj = ((NodeList)aPackage.get("name")).get(0);
        Object descObj = ((NodeList)aPackage.get("packagedescr")).get(0);
        Object versionObj = ((NodeList)aPackage.get("version")).get(0);
        Object fileNameObj = ((NodeList)aPackage.get("filename")).get(0);
        Object filePathObj = ((NodeList)aPackage.get("filepath")).get(0);

        String name = ((Node)nameObj).text();
        String desc = ((Node)descObj).text();
        String version = ((Node)versionObj).text();
        String fileName = ((Node)fileNameObj).text();
        String filePath = ((Node)filePathObj).text();

        Package newPackage;
        if (packages.containsKey(name)) {
          newPackage = packages.get(name);
        } else {
          newPackage = new Package(this, name, desc);
          packages.put(name, newPackage);
        }
        Version newVersion = new Version(newPackage, versionType, version, filePath, fileName);
        newPackage.addVersion(newVersion);

        // what are the dependencies
        for (Object dependencyObj : ((Node)aPackage.getAt(new QName("dependencies")).get(0)).children()) {
          if (dependencyObj instanceof Node) {
            Node dependency = (Node) dependencyObj;

            Object depNameObj = ((NodeList)dependency.get("name")).get(0);
            Object depTypeObj = ((NodeList)dependency.get("deptype")).get(0);
            String depName = ((Node)depNameObj).text();
            DependencyType depType = DependencyType.valueOf(((Node) depTypeObj).text().toUpperCase());
            newVersion.addDependency(new Dependency(depName, depType));
          }
        }
      }
    }
  }

  public AppositeStore getAppositeStore() {
    return appositeStore;
  }

  public void setAppositeStore(AppositeStore appositeStore) {
    this.appositeStore = appositeStore;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getSet() {
    return set;
  }

  public void setSet(String set) {
    this.set = set;
  }

  public Map<String, Package> getPackages() {
    return Collections.unmodifiableMap(packages);
  }
}
