package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.*;
import net.sunxu.demo.sb.entity.FastDFSFile;
import net.sunxu.demo.sb.entity.SocialLink;
import net.sunxu.demo.sb.entity.User;
import net.sunxu.demo.sb.entity.UserEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface UserManageService {
    UserBO getUser(Long userId);

    Page<UserBO> rankUser(UserRankType type, Pageable pageable);

    UserDetailBO getUserDetail(Long userId);

    Page<UserBO> listUsers(Pageable option);

    void updateUser(UserEditBO user);

    void updateUserAvatar(UserBO user, FastDFSFile fastDFSFile);

    void changeUserName(UserBO user, String newUserName);

    boolean isUserNameExist(String userName);

    void lockUser(UserBO user);

    void unlockUser(UserBO user);

    void deleteUser(UserBO user);

    Page<UserEventBO> listUserEvents(Long userId, Pageable option);

    List<ViewHistoryBO> getViewHistory(Long userId, Date startTime, Date endTime);

}
