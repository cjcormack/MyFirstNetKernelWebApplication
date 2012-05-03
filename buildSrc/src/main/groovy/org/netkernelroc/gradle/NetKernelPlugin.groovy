package org.netkernelroc.gradle;

import org.gradle.api.Plugin
import org.gradle.api.Project


class NetKernelPlugin implements Plugin<Project> {

  @Override
  void apply(Project p) {
    p.convention.plugins.netkernel = new NetKernelConvention(p)
    p.task('nkDownloadPackages') << {
      p.convention.plugins.netkernel.downloadPackages();
    }
  }
}