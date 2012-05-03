package org.netkernelroc.gradle.model;

public class Dependency {
  private String name;
  private DependencyType type;

  public Dependency(String name, DependencyType type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DependencyType getType() {
    return type;
  }

  public void setType(DependencyType type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if ((o instanceof Dependency) && o != null) {
      Dependency otherDependency = (Dependency)o;
      return otherDependency.getName().equals(this) && otherDependency.getType() == this.getType();
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return name.hashCode() + type.hashCode();
  }

  @Override
  public String toString() {
    return name + " [" + type + "]";
  }
}
