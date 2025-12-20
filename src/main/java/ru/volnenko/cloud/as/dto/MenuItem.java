package ru.volnenko.cloud.as.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class MenuItem {

    @NonNull
    private String name = "";

    @NonNull
    private String link = "";

    @NonNull
    private Boolean line = false;

    @NonNull
    private Boolean show = true;

}
