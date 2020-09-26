ARG DEBIAN_VERSION=buster-slim
ARG MILL_VERSION=0.7.4

FROM debian:${DEBIAN_VERSION}

RUN apt-get update
RUN apt-get install openjdk-8-jre
RUN apt-get install curl
RUN curl -L https://github.com/lihaoyi/mill/releases/download/${MILL_VERSION}/${MILL_VERSION} > /usr/local/bin/mill && chmod +x /usr/local/bin/mill

