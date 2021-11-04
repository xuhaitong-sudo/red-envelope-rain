package com.group11.controller;

import com.group11.common.utils.R;
import com.group11.pojo.dto.Envelope;
import com.group11.pojo.vo.GetWalletListResponse;
import com.group11.pojo.vo.OpenResponse;
import com.group11.pojo.vo.SnatchResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Xu Haitong
 * @since 2021/11/3 13:31
 */
@RestController
public class Controller {

    @GetMapping("/")
    public String hello() {
        return "服务成功启动";
    }


    @PostMapping("/snatch")
    public R snatch(@RequestParam("uid") Long uid) {
        System.out.println("接受到的 uid 是 ：" + uid);

        Random random = new Random();
        SnatchResponse response = new SnatchResponse(123L, 5L, (long) random.nextInt(1000));
        return R.ok().put("data", response);
    }


    @PostMapping("/open")
    public R open(@RequestParam("uid") Long uid, @RequestParam("envelope_id") Long envelopeId) {
        System.out.println("接受到的 uid, envelopeId 是 ：" + uid + ", " + envelopeId);

        Random random = new Random();
        return R.ok().put("data", new OpenResponse((long) random.nextInt(1000)));
    }

    @PostMapping("/get_wallet_list")
    public R getWalletList(@RequestParam("uid") Long uid) {
        System.out.println("接受到的 uid 是 ：" + uid);

        Random random = new Random();
        List<Envelope> list = new ArrayList<>();

        list.add(new Envelope(123L, (long) random.nextInt(1000), true, (long) random.nextInt(1000)));
        list.add(new Envelope(123L, null, false, (long) random.nextInt(1000)));
        return R.ok().put("data", new GetWalletListResponse((long) random.nextInt(1000), list));
    }
}
