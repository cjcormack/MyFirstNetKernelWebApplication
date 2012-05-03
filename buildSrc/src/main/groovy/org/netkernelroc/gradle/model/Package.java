package org.netkernelroc.gradle.model;

import java.util.*;

public class Package {
  private Repository repository;
  private String name;
  private String description;
  private SortedSet<Version> versions = new TreeSet<Version>();

  public Package(Repository repository, String name, String description) {
    this.repository = repository;
    this.name = name;
    this.description = description;
  }

  public Repository getRepository() {
    return repository;
  }

  public void setRepository(Repository repository) {
    this.repository = repository;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SortedSet<Version> getVersions() {
    return Collections.unmodifiableSortedSet(versions);
  }

  public boolean addVersion(Version version) {
    return versions.add(version);
  }

  public boolean removeVersion(Object version) {
    return versions.remove(version);
  }

  @Override
  public String toString() {
    return name + " (" + versions.last().getVersionString() + ")";
  }
}
