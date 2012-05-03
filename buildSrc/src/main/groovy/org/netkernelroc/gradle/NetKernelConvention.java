package org.netkernelroc.gradle;

import groovy.lang.Closure;
import org.gradle.api.Project;
import org.netkernelroc.gradle.model.*;
import org.netkernelroc.gradle.model.Package;

import java.io.*;
import java.net.URL;
import java.util.*;

public class NetKernelConvention {
  private String packageDownloadStore = "nk-download";
  private AppositeStore appositeStore = new AppositeStore();
  private Set<Package> packages = new HashSet<Package>();

  public NetKernelConvention(Project p) {
  }

  public void nkRepositories(Closure closure) {
    closure.setDelegate(this);
    closure.call();
  }

	public void defineRepository(Map<String, String> map) {
    if(map.get("baseUrl") != null && map.get("path") != null && map.get("set") != null) {
      appositeStore.addRepository(new Repository(appositeStore, map.get("baseUrl"), map.get("path"), map.get("set")));
    } else {
      System.out.println("WARNING: Specified repository: map must include 'baseUrl', 'path' and 'set' attributes.");
    }
	}

  public void definePackage(Map<String, String> map) {
    Package aPackage = appositeStore.getAllPackages().get(map.get("name"));
    packages.add(aPackage);
    packages.addAll(aPackage.getVersions().last().allDependencies());
  }

  public void downloadPackages() throws Exception {
    File packageDownloadStoreFile = new File(packageDownloadStore);
    if (!packageDownloadStoreFile.exists()) {
      packageDownloadStoreFile.mkdirs();
    }

    for (Package dependentPackage : packages) {
      System.out.println("Downloading " + dependentPackage.getVersions().last().getDownloadUrl());

      OutputStream out = null;
      InputStream in = null;
      try {
        out = new FileOutputStream(new File(packageDownloadStoreFile, dependentPackage.getName() + ".nkp.jar"));
        in = new URL(dependentPackage.getVersions().last().getDownloadUrl()).openStream();

        copyStream(in, out);
      } finally {
        try {
          if (out != null){
            out.close();
          }
        } finally {
          if (in != null){
            in.close();
          }
        }
      }
    }
  }

  public Set<Package> getPackages() {
       return Collections.unmodifiableSet(packages);
     }

  public void setPackages(Set<Package> packages) {
    this.packages = packages;
  }

  public static void copyStream(InputStream input, OutputStream output)  throws IOException {
    byte[] buffer = new byte[1024]; // Adjust if you want
    int bytesRead;
    while ((bytesRead = input.read(buffer)) != -1) {
        output.write(buffer, 0, bytesRead);
    }
  }
}
