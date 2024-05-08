package com.itljx.checkup.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
  private Long id;
  private Long userId;
  private Long infoId;
  private Long isRead;
}
