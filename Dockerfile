ARG UBUNTU_VERSION=18.04
ARG MILL_VERSION=0.7.4

FROM ubuntu:${UBUNTU_VERSION}

RUN apt-get update
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y curl
RUN curl -L -o /usr/local/bin/mill https://github.com/lihaoyi/mill/releases/download/$MILL_VERSION/$MILL_VERSION
RUN ls /usr/local/bin/
RUN chmod +x /usr/local/bin/mill
RUN touch build.sc
RUN /usr/local/bin/mill -i resolve _
RUN rm build.sc

