package org.netkernelroc.gradle.model;

import java.util.*;

public class Version implements Comparable<Version> {
  private Package aPackage;
  private VersionType versionType;
  private int major;
  private int minor;
  private int patch;
  private String filePath;
  private String fileName;
  private List<Dependency> dependencies = new ArrayList<Dependency>();

  public Version(Package aPackage, VersionType versionType, String version, String filePath, String fileName) {
    this.aPackage = aPackage;
    this.versionType = versionType;
    this.filePath = filePath;
    this.fileName = fileName;

    String[] versionParts = version.split("\\.");
    this.major = Integer.parseInt(versionParts[0]);
    this.minor = Integer.parseInt(versionParts[1]);
    this.patch = Integer.parseInt(versionParts[2]);
  }

  public Package getPackage() {
    return aPackage;
  }

  public void setPackage(Package aPackage) {
    this.aPackage = aPackage;
  }

  public VersionType getVersionType() {
    return versionType;
  }

  public void setVersionType(VersionType versionType) {
    this.versionType = versionType;
  }

  public int getMajor() {
    return major;
  }

  public void setMajor(int major) {
    this.major = major;
  }

  public int getMinor() {
    return minor;
  }

  public void setMinor(int minor) {
    this.minor = minor;
  }

  public int getPatch() {
    return patch;
  }

  public void setPatch(int patch) {
    this.patch = patch;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public boolean addDependency(Dependency dependency) {
    return dependencies.add(dependency);
  }

  public List<Dependency> getDependencies() {
    return Collections.unmodifiableList(dependencies);
  }

  public void setDependencies(List<Dependency> dependencies) {
    this.dependencies = dependencies;
  }

  public String getDownloadUrl() {
    return aPackage.getRepository().getBaseUrl() + "/" + filePath + fileName;
  }

  public Set<Package> allDependencies() {
    Set<Package> allDependencies = new HashSet<Package>();

    AppositeStore appositeStore = aPackage.getRepository().getAppositeStore();

    for (Dependency dependency : dependencies) {
      Package dependentPackage = appositeStore.getAllPackages().get(dependency.getName());
      if (dependentPackage == null) {
        System.err.println("WARNING: Dependent package '" + dependency.getName() + "' does not exist in any of the repositories that have been supplied.");
      } else {
        allDependencies.add(dependentPackage);
        allDependencies.addAll(dependentPackage.getVersions().last().allDependencies());
      }
    }

    return allDependencies;
  }

  @Override
  public int compareTo(Version otherVersion) {
    if (this.major == otherVersion.major && this.minor == otherVersion.minor && otherVersion.patch == this.patch) {
      return 0;
    } else {
      if ((this.major > otherVersion.major) ||
          (this.major == otherVersion.major && this.minor > otherVersion.minor) ||
          (this.major == otherVersion.major && this.minor == otherVersion.minor && this.patch > otherVersion.patch)) {
        return 1;
      } else {
        return -1;
      }
    }
  }

  public String getVersionString() {
    return major + "." + minor + "." + patch;
  }

  @Override
  public String toString() {
    return major + "." + minor + "." + patch + " [" + versionType + "]";
  }
}
