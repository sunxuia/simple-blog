package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.entity.Notify;
import net.sunxu.demo.sb.entity.NotifyType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifyServiceImpl implements NotifyService {
    @Override
    public boolean shouldReceive(NotifyType notifyType) {
        return false;
    }

    @Override
    public int getUnreadCount(NotifyCategory notifyType, Long userId) {
        return 0;
    }

    @Override
    public List<Notify> readNotify(NotifyCategory notifyType) {
        return null;
    }

    @Override
    public void createNotify(Notify notify) {

    }

    @Override
    public void deleteNotify(Long notifyId) {

    }

    @Override
    public void setReceiveNotify(NotifyType notifyType, Long userId) {

    }

    @Override
    public void setNotReceiveNotify(NotifyType notifyType, Long userId) {

    }
}
