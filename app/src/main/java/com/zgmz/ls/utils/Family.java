package com.zgmz.ls.utils;

import java.util.ArrayList;

public class Family {
    public FamilyInfo fInfo; //家庭信息
    public CheckInfo cInfo = null; //核查信息
    public ArrayList<FamilyMember> cyxx = new ArrayList<FamilyMember>(); //成员信息
    public ArrayList<Meterial> zmclxx = new ArrayList<Meterial>(); //证明材料信息

    public String ToJSONString() {
        //return "{\"qty\":100,\"name\":\"iPad 4\"}";
        StringBuilder builder = new StringBuilder();

        // cyxx
        for (FamilyMember i : cyxx) {
            builder.append("{ ");
            builder.append(i.ToJSONString());
            builder.append(" }");
            builder.append(", ");
        }
        String cyxxString = builder.toString();
        if (cyxxString.length() > 0) {
            cyxxString = cyxxString.substring(0, cyxxString.length() - 2); // trim last ,
        }

        // zmclxx
        builder = new StringBuilder();
        for (Meterial i : zmclxx) {
            builder.append("{ ");
            builder.append(i.ToJSONString());
            builder.append(" }");
            builder.append(", ");
        }
        String zmclxxString = builder.toString();
        if (zmclxxString.length() > 0) {
            zmclxxString = zmclxxString.substring(0, zmclxxString.length() - 2); // trim last ,
        }

        if (cInfo == null) {
            return String.format("{%s, \"cyxx\" : [ %s ], \"zmclxx\" : [ %s ]}", fInfo.ToJSONString(), cyxxString, zmclxxString);
        }
        else {
            return String.format("{%s, %s, \"cyxx\" : [ %s ], \"zmclxx\" : [ %s ]}", fInfo.ToJSONString(), cInfo.ToJSONString(), cyxxString, zmclxxString);
        }
    }
}
