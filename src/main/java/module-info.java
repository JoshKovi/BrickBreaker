module BrickBreaker {
    requires ServerCommon;
    requires PortWebDatabase;
    requires Logger;
    requires jdk.httpserver;
    requires java.net.http;
    requires java.desktop;
    exports com.kovisoft.brickbreaker.exports;
    exports com.kovisoft.brickbreaker.exports.records;

    opens com.kovisoft.brickbreaker.exports.records to PortWebDatabase;
}