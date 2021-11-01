FROM java:8
COPY *.jar /root/server/app.jar
CMD ["--server.port=8080"]
EXPOSE 8080
ENTRYPOINT ["java","-jar","/root/server/app.jar"]
