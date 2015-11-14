package com.aalto.precious.sensing;

/**
 * Created by fida on 6.11.2015.
 */
public class Request {
    private RequestType requestType;
    private SensorType sensorType;
    private ValueType valueType;


    public Request() {
        this.requestType = RequestType.DEFAULT;
        this.sensorType = SensorType.ALL;
        this.valueType = ValueType.DEFAULT;
    }

    public String getStringRequest() {
        return this.requestType.getString();
    }


    public enum RequestType {
        SENSORLIST {
            public String getString() {
                String string = "sensor_list \n";
                return string;
            }
        },
        SENSORSVALUE {
            public String getString() {
                String string = "sensors_value \n";
                return string;
            }
        },
        DEFAULT {
            public String getString() {
                String string = "<sensors_value><sensor_type>all</sensor_type>" +
                        "<value_type>default</value_type></sensors_value> ";
                return string;
            }
        };

        public abstract String getString();
    }

    public enum SensorType {
        TEMPERATURE {
            public String getString() {
                String string = "<sensor_type> temperature </sensor_type> \n";
                return string;
            }
        },
        HUMIDITY {
            public String getString() {
                String string = "<sensor_type> humidity </sensor_type> \n";
                return string;
            }
        },
        LIGHT {
            public String getString() {
                String string = "<sensor_type> light </sensor_type> \n";
                return string;
            }
        },
        ALL {
            public String getString() {
                String string = "<sensor_type> all </sensor_type> \n";
                return string;
            }
        };

        public abstract String getString();
    }

    public enum ValueType {
        DEFAULT {
            public String getString() {
                String string = "<value_type> default </value_type> \n";
                return string;
            }
        },
        CURRENT {
            public String getString() {
                String string = "<value_type> current </value_type> \n";
                return string;
            }
        },
        ALL {
            public String getString() {
                String string = "<value_type> all </value_type> \n";
                return string;
            }
        };

        public abstract String getString();
    }
}
