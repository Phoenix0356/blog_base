package com.phoenix.blog.enumeration;

import com.phoenix.blog.exceptions.clientException.EnumTypeNotFoundException;
import com.phoenix.blog.exceptions.clientException.UserNotFoundException;
import lombok.Getter;

@Getter
public enum MessageType {
    UPVOTE("UPVOTE",1),
    BOOKMARK("BOOKMARK",2),
    BOOKMARK_CANCEL("BOOKMARK_CANCEL",3);

    private final String name;
    private final int type;

    MessageType(String name,int type){
        this.name = name;
        this.type = type;
    }

    public static MessageType valueOf(int type) {
        for (MessageType messageType:MessageType.values()) {
            if (messageType.type == type) {
                return messageType;
            }
        }
        throw new EnumTypeNotFoundException();
    }


}
