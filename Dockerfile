FROM gradle:8.12.0-jdk17 AS builder

ENV ANDROID_SDK_URL https://dl.google.com/android/repository/commandlinetools-linux-7302050_latest.zip
ENV ANDROID_HOME  /usr/local/android-sdk-linux
ENV PATH ${PATH}:${ANDROID_HOME}/cmdline-tools/bin:${ANDROID_HOME}/platform-tools
ENV ANDROID_CERT_PASSWORD "testtest"

#Install Android
RUN mkdir -p "$ANDROID_HOME" .android && \
    cd "$ANDROID_HOME" && \
    curl -o sdk.zip $ANDROID_SDK_URL && \
    unzip sdk.zip -d cmdline-tools && \
    mv cmdline-tools/cmdline-tools cmdline-tools/latest && \
    rm sdk.zip

# Accept licenses and install necessary Android components
RUN yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_HOME --licenses
RUN ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_HOME --update
RUN ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_HOME "platforms;android-35"\
        "platform-tools" \
        "build-tools;33.0.0" \
        "platforms;android-33"



#WORKDIR /home/gradle


#COPY . .
#
#RUN chmod +x ./gradlew

#RUN ./gradlew assembleDebug

#CMD ["gradle", "assembleRelease"]



