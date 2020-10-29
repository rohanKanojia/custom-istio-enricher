# Custom Enricher Using Fabric8 Maven Plugin/Eclipse JKube

This repository contains two modules:
- app
- custom istio enricher

# How to Build:
Just need to run:
```bash
mvn clean install
```

# How to Run:
This project demonstrates use of Custom Enricher. You can check `pom.xml` of `app/` project to see how Custom Enricher is integrated into it. When you would run resource goal, you should be able to see enricher in action:

```
custom-istio-enricher : $ cd app/
app : $ ls
fabric8-maven-sample-custom-enricher-app.iml  pom.xml  pom.xml.versionsBackup  src  target
app : $ mvn fabric8:resource
[INFO] Scanning for projects...
[INFO] 
[INFO] --< io.fabric8.samples.custom-enricher:fabric8-maven-sample-custom-enricher-app >--
[INFO] Building Fabric8 Maven :: Sample :: Custom Enricher :: App 4.5-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- fabric8-maven-plugin:4.3.1:resource (default-cli) @ fabric8-maven-sample-custom-enricher-app ---
[WARNING] F8: Cannot access cluster for detecting mode: Broken pipe (Write failed)
[INFO] F8: Running generator spring-boot
[INFO] F8: spring-boot: Using Container image fabric8/java-centos-openjdk8-jdk:1.5 as base / builder
[INFO] F8: using resource templates from /home/rohaan/work/repos/custom-istio-enricher/app/src/main/fabric8
[INFO] F8: fmp-service: Adding a default service 'fabric8-maven-sample-custom-enricher-app' with ports [8080]
[INFO] F8: fmp-revision-history: Adding revision history limit to 2
[INFO] F8: istio-enricher:  Istio Enricher: Added dummy Gateway
[INFO] F8: validating /home/rohaan/work/repos/custom-istio-enricher/app/target/classes/META-INF/fabric8/kubernetes/custom-enricher.yml resource
[WARNING] F8: Failed to validate resources: null
[INFO] F8: using resource templates from /home/rohaan/work/repos/custom-istio-enricher/app/src/main/fabric8
[INFO] F8: fmp-service: Adding a default service 'fabric8-maven-sample-custom-enricher-app' with ports [8080]
[INFO] F8: fmp-revision-history: Adding revision history limit to 2
[INFO] F8: istio-enricher:  Istio Enricher: Added dummy Gateway
[INFO] F8: validating /home/rohaan/work/repos/custom-istio-enricher/app/target/classes/META-INF/fabric8/openshift/custom-enricher.yml resource
[WARNING] F8: Failed to validate resources: null
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.738 s
[INFO] Finished at: 2020-10-29T22:21:28+05:30
[INFO] ------------------------------------------------------------------------
```
After running resource goal, you should be able to see a dummy `Gateway` manifest in target directory:
```
app : $ cat target/classes/META-INF/fabric8/kubernetes/custom-enricher.yml 
---
apiVersion: networking.istio.io/v1alpha3
kind: Gateway
metadata:
  labels:
    app: fabric8-maven-sample-custom-enricher-app
    provider: fabric8
    version: 4.5-SNAPSHOT
    group: io.fabric8.samples.custom-enricher
  name: custom-enricher
spec:
  selector:
    app: test-app
  servers:
  - hosts:
    - uk.bookinfo.com
    - in.bookinfo.com
    port:
      name: http
      number: 80
      protocol: HTTP
    tls:
      httpsRedirect: true
```