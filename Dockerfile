ARG UBUNTU_VERSION=18.04
ARG MILL_VERSION=0.7.4

FROM ubuntu:${UBUNTU_VERSION}

RUN apt-get update
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y curl
RUN curl -L https://github.com/lihaoyi/mill/releases/download/${MILL_VERSION}/${MILL_VERSION} > /usr/local/bin/mill && chmod +x /usr/local/bin/mill

