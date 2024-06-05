package com.phoenix.blog.enumeration;

import com.phoenix.blog.exceptions.clientException.EnumTypeNotFoundException;
import lombok.Getter;

@Getter
public enum MessageType {
    UPVOTE("UPVOTE",0b1),
    BOOKMARK("BOOKMARK",0b10),
    BOOKMARK_CANCEL("BOOKMARK_CANCEL",0b100);

    private final String name;
    private final int typeNum;


    MessageType(String name,int typeNum){
        this.name = name;
        this.typeNum = typeNum;
    }

    public static MessageType valueOf(int type) {
        for (MessageType messageType:MessageType.values()) {
            if (messageType.typeNum == type) {
                return messageType;
            }
        }
        throw new EnumTypeNotFoundException();
    }


}
