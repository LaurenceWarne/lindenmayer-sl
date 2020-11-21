FROM ubuntu:18.04

ARG MILL_VERSION=0.7.4

USER root

RUN apt-get update
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y curl
# Install mill
RUN \
  curl -L -o /usr/local/bin/mill https://github.com/lihaoyi/mill/releases/download/${MILL_VERSION}/${MILL_VERSION} && \
  chmod +x /usr/local/bin/mill && \
  touch build.sc && \
  mill -i resolve _ && \
  rm build.sc

