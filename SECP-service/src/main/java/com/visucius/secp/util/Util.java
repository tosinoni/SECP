package com.visucius.secp.util;

import com.visucius.secp.models.ActionType;
import com.visucius.secp.models.Record;
import com.visucius.secp.models.User;

import java.util.Collection;
import java.util.Date;

public class Util {
    public static <E> void addAllIfNotNull(Collection<E> list, Collection<? extends E> c) {
        if (c != null) {
            list.addAll(c);
        }
    }

    public static<E> boolean isCollectionEmpty(Collection<E> list) {
        return list == null || list.isEmpty();
    }

    public static Record createRecord(User editor, ActionType actionType, String action) {
        String editorName = editor.getFirstname() + " " + editor.getLastname();
        return new Record(editorName, actionType, action, new Date());
    }
}
