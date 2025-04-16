FROM gradle:8.12.0-jdk17 AS builder

ENV DEBIAN_FRONTEND=noninteractive

ENV ANDROID_SDK_URL=https://dl.google.com/android/repository/commandlinetools-linux-7302050_latest.zip
ENV ANDROID_HOME=/usr/local/android-sdk-linux
ENV PATH=${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools
ENV ANDROID_SDK_ROOT=${ANDROID_HOME}
ENV SOURCE_DATA_EPOC=0
ENV TZ=UTC
ENV ANDROID_CERT_PASSWORD="testtest"

#Install Android
RUN mkdir -p "$ANDROID_HOME" ~/.android && \
    cd "$ANDROID_HOME" && \
    curl -sSL -o sdk.zip $ANDROID_SDK_URL && \
    unzip -q sdk.zip -d cmdline-tools && \
    mv cmdline-tools/cmdline-tools cmdline-tools/latest && \
    rm sdk.zip

# Accept licenses and install necessary Android components
#RUN yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_HOME --licenses && \
#    ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_HOME --update && \
#    ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_HOME  \
#        "platforms;android-35" \
#        "platform-tools" \
#        "build-tools;33.0.0" \

RUN yes | sdkmanager --licenses && \
    sdkmanager --update && \
    sdkmanager \
        "platform-tools" \
        "platforms;android-34" \
        "build-tools;34.0.0"

WORKDIR /app
COPY . .

RUN chmod +x ./gradlew && \
    find . -type f -exec touch -d "@${SOURCE_DATE_EPOCH}" {} + || true


# ./gradlew assembleDebug

CMD ["gradle", "bundleRelease"]



