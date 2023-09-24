FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER kxmall

RUN mkdir -p /kxmall/server/logs \
    /kxmall/server/temp \
    /kxmall/skywalking/agent

WORKDIR /kxmall/server

ENV SERVER_PORT=8585

EXPOSE ${SERVER_PORT}

ADD ./target/kxmall-admin.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
            # 应用名称 如果想区分集群节点监控 改成不同的名称即可
#            "-Dskywalking.agent.service_name=kxmall-server", \
#            "-javaagent:/kxmall/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
