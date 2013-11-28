package com.patternminers;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.patternminers.data.Order;

public class OrderHistory {
    private HashMultimap<String, String> rp = HashMultimap.create();

    public OrderHistory(List<Order> orders) {
        for (Order o : orders) {
            rp.put(o.getUser(), o.getSku());
        }
    }

    public List<String> getOrdered(String user) {
        return Lists.newArrayList(rp.get(user));
    }

}
