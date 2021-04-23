package com.bavantha.ripod;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

public class ModeTalker extends AbstractNodeMain {
    private String topic_name;
    private String val, oldVal;

    public ModeTalker(String topic) {
        this.topic_name = topic;
    }

    public GraphName getDefaultNodeName() {
        return GraphName.of("android/control_val");
    }

    public void onStart(ConnectedNode connectedNode) {
        final Publisher<std_msgs.String> publisher = connectedNode.newPublisher(this.topic_name, "std_msgs/String");
        connectedNode.executeCancellableLoop(new CancellableLoop() {

            protected void setup() {
                val="None";
            }

            protected void loop() throws InterruptedException {
                std_msgs.String str = (std_msgs.String)publisher.newMessage();
                val= OptionsActivity.getMode() +":"+ OptionsActivity.getHeight() +":"+ OptionsActivity.getPan() +":"+ OptionsActivity.getTilt() +":"+ OptionsActivity.getLight() +":"+ OptionsActivity.getVideoState() +":"+ OptionsActivity.getPreset();
                str.setData(connectedNode.getCurrentTime() +"|" + val);
                if(!val.equals(oldVal)) {
                    publisher.publish(str);
                }
                oldVal= val;
                Thread.sleep(20L);
            }
        });
    }
}
