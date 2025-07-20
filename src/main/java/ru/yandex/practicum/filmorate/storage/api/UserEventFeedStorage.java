package ru.yandex.practicum.filmorate.storage.api;

import ru.yandex.practicum.filmorate.model.entity.UserEventFeed;

import java.util.List;

public interface UserEventFeedStorage {

    void saveEvent(UserEventFeed userEventFeed);

    List<UserEventFeed> getLastUserEvents(Long userId);
}
