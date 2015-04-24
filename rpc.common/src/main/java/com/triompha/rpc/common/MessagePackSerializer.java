package com.triompha.rpc.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.laiwang.idl.Converter;
import com.laiwang.idl.MessageReader;
import com.laiwang.idl.MessageWriter;
import com.laiwang.idl.msgpacklite.unpacker.StreamInput;

public class MessagePackSerializer implements Serializer {

    public Object deserialize(byte[] bytes, Type t) throws IOException, Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        MessageReader reader = new MessageReader(new StreamInput(byteArrayInputStream, bytes.length));
        return Converter.castByType(t, reader.getNextValue());
    }

    public Object[] deSerialize(byte[] bytes, Type[] ts) throws IOException, Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        MessageReader reader = new MessageReader(new StreamInput(byteArrayInputStream, bytes.length));
        List<Object> oriParameters = new ArrayList<Object>();
        while (reader.hasMore()) {
            oriParameters.add(reader.getNextValue());
        }
        if (oriParameters.size() != ts.length) {
            throw new Exception("pack error");
        }

        return Converter.batchCast(ts, oriParameters.toArray());
    }

    public byte[] serialize(Object obj) throws IOException, Exception {
        MessageWriter packWriter = new MessageWriter();
        packWriter.write(obj);
        byte[] bytes = packWriter.toByteArray();
        return bytes;
    }

}
