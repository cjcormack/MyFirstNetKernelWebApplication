package org.netkernelroc.gradle.model;

import java.util.*;

public class AppositeStore {
  private Set<Repository> repositories = new HashSet<Repository>();
  private Map<String, Package> allPackages = new HashMap<String, Package>();

  public void addRepository(Repository repository) {
    repositories.add(repository);
    allPackages.putAll(repository.getPackages());
  }

  public Set<Repository> getRepositories() {
    return Collections.unmodifiableSet(repositories);
  }

  public Map<String, Package> getAllPackages() {
    return Collections.unmodifiableMap(allPackages);
  }
}
