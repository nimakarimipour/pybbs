package co.yiiu.pybbs.config.websocket;

import co.yiiu.pybbs.util.Message;
import com.alibaba.fastjson.JSON;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {
    @Override
    public Message decode(String s) {
        return JSON.parseObject(s, Message.class);
    }

    @Override
    public boolean willDecode(String s) {
        // aajsonaaaaaaa，aaaaaadecode()aaaaaa，aaaaaaaa
        return JSON.isValid(s);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
