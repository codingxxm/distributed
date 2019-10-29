package com.xxm.confrim;

import com.rabbitmq.client.ConfirmListener;

import java.io.IOException;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/10/29 21:14
 **/
public class MyConfirmListener implements ConfirmListener {
    @Override
    public void handleAck(long l, boolean b) throws IOException {
        System.out.println("msg send success");
    }

    @Override
    public void handleNack(long l, boolean b) throws IOException {
        System.out.println("msg send failure");
    }
}
