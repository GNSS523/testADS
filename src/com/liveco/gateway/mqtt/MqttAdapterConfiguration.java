package com.liveco.gateway.mqtt;

public class MqttAdapterConfiguration implements Cloneable {

    /** connection string for the MQTT broker (eg: "tcp://iot.eclipse.org:1883") */
    private String mqttBroker;

    /** the username for authenticating against the MQTT broker */
    private String mqttUsername;

    /** the password for authenticating agains the MQTT broker */
    private String mqttPassword;

    /** the client id for connecting to the MQTT broker; the id of the Indego mapper */
    private String mqttClientId;

    /** the root topic (namespace) where to put the Indego topics */
    private String mqttTopicRoot;

    /** the quality of service to use for communicating to the MQTT broker (see MQTT spec) */
    private int mqttQos = 1;
    
    /** Base url of the Indego web service (if different from default) */
    private String indegoBaseUrl;

    /** the username for connecting to the Indego server */
    private String indegoUsername;

    /** the password for connecting to the Indego server */
    private String indegoPassword;

    /** the polling interval (in ms) */
    private int pollingIntervalMs;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone ()
    {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Gets the connection string for the MQTT broker (eg: "tcp://iot.eclipse.org:1883").
     *
     * @return the connection string for the MQTT broker (eg: "tcp://iot.eclipse.org:1883")
     */
    public String getMqttBroker ()
    {
        return mqttBroker;
    }

    /**
     * Sets the connection string for the MQTT broker (eg: "tcp://iot.eclipse.org:1883").
     *
     * @param mqttBroker the new connection string for the MQTT broker (eg: "tcp://iot.eclipse.org:1883")
     */
    public void setMqttBroker (String mqttBroker)
    {
        this.mqttBroker = mqttBroker;
    }

    /**
     * Gets the username for authenticating against the MQTT broker.
     *
     * @return the username for authenticating against the MQTT broker
     */
    public String getMqttUsername ()
    {
        return mqttUsername;
    }

    /**
     * Sets the username for authenticating against the MQTT broker.
     *
     * @param mqttUsername the new username for authenticating against the MQTT broker
     */
    public void setMqttUsername (String mqttUsername)
    {
        this.mqttUsername = mqttUsername;
    }

    /**
     * Gets the password for authenticating agains the MQTT broker.
     *
     * @return the password for authenticating agains the MQTT broker
     */
    public String getMqttPassword ()
    {
        return mqttPassword;
    }

    /**
     * Sets the password for authenticating agains the MQTT broker.
     *
     * @param mqttPassword the new password for authenticating agains the MQTT broker
     */
    public void setMqttPassword (String mqttPassword)
    {
        this.mqttPassword = mqttPassword;
    }

    /**
     * Gets the client id for connecting to the MQTT broker; the id of the Indego mapper.
     *
     * @return the client id for connecting to the MQTT broker; the id of the Indego mapper
     */
    public String getMqttClientId ()
    {
        return mqttClientId;
    }

    /**
     * Sets the client id for connecting to the MQTT broker; the id of the Indego mapper.
     *
     * @param mqttClientId the new client id for connecting to the MQTT broker; the id of the Indego mapper
     */
    public void setMqttClientId (String mqttClientId)
    {
        this.mqttClientId = mqttClientId;
    }

    /**
     * Gets the root topic (namespace) where to put the Indego topics.
     *
     * @return the root topic (namespace) where to put the Indego topics
     */
    public String getMqttTopicRoot ()
    {
        return mqttTopicRoot;
    }

    /**
     * Sets the root topic (namespace) where to put the Indego topics. If the root does
     * not have an ending "/", this is added.
     *
     * @param mqttTopicRoot the new root topic (namespace) where to put the Indego topics
     */
    public void setMqttTopicRoot (String mqttTopicRoot)
    {
        if ( !mqttTopicRoot.endsWith("/") ) {
            mqttTopicRoot = mqttTopicRoot + "/";
        }
        this.mqttTopicRoot = mqttTopicRoot;
    }

    /**
     * Gets the quality of service to use for communicating to the MQTT broker (see MQTT spec).
     *
     * @return the quality of service to use for communicating to the MQTT broker (see MQTT spec)
     */
    public int getMqttQos ()
    {
        return mqttQos;
    }

    /**
     * Sets the quality of service to use for communicating to the MQTT broker (see MQTT spec).
     *
     * @param mqttQos the new quality of service to use for communicating to the MQTT broker (see MQTT spec)
     */
    public void setMqttQos (int mqttQos)
    {
        this.mqttQos = mqttQos;
    }
    
    /**
     * Gets the base url of the Indego web service (if different from default)
     *
     * @return the base url of the Indego web service (if different from default)
     */
    public String getIndegoBaseUrl ()
    {
        return indegoBaseUrl;
    }

    /**
     * Sets the base url of the Indego web service (if different from default)
     *
     * @param indegoBaseUrl the base url of the Indego web service (if different from default) 
     */
    public void setIndegoBaseUrl (String indegoBaseUrl)
    {
        this.indegoBaseUrl = indegoBaseUrl;
    }

    /**
     * Gets the username for connecting to the Indego server.
     *
     * @return the username for connecting to the Indego server
     */
    public String getIndegoUsername ()
    {
        return indegoUsername;
    }

    /**
     * Sets the username for connecting to the Indego server.
     *
     * @param indegoUsername the new username for connecting to the Indego server
     */
    public void setIndegoUsername (String indegoUsername)
    {
        this.indegoUsername = indegoUsername;
    }

    /**
     * Gets the password for connecting to the Indego server.
     *
     * @return the password for connecting to the Indego server
     */
    public String getIndegoPassword ()
    {
        return indegoPassword;
    }

    /**
     * Sets the password for connecting to the Indego server.
     *
     * @param indegoPassword the new password for connecting to the Indego server
     */
    public void setIndegoPassword (String indegoPassword)
    {
        this.indegoPassword = indegoPassword;
    }

    /**
     * Gets the polling interval (in ms).
     *
     * @return the polling interval (in ms)
     */
    public int getPollingIntervalMs ()
    {
        return pollingIntervalMs;
    }

    /**
     * Sets the polling interval (in ms).
     *
     * @param pollingIntervalMs the new polling interval (in ms)
     */
    public void setPollingIntervalMs (int pollingIntervalMs)
    {
        this.pollingIntervalMs = pollingIntervalMs;
    }

}

