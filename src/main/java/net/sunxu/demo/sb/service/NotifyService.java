package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.entity.Notify;
import net.sunxu.demo.sb.entity.NotifyType;

import java.util.List;

public interface NotifyService {

    boolean shouldReceive(NotifyType notifyType);

    int getUnreadCount(NotifyCategory notifyType, Long userId);

    List<Notify> readNotify(NotifyCategory notifyType);

    void createNotify(Notify notify);

    void deleteNotify(Long notifyId);

    void setReceiveNotify(NotifyType notifyType, Long userId);

    void setNotReceiveNotify(NotifyType notifyType, Long userId);
}
