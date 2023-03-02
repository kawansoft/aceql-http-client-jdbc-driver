/*
 * This file is part of AceQL JDBC Driver.
 * AceQL JDBC Driver: Remote JDBC access over HTTP with AceQL HTTP.
 * Copyright (c) 2023,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aceql.jdbc.commons.test.base.dml;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.aceql.jdbc.commons.test.connection.ConnectionParms;

/**
 * Values to use for the tests comparison of values with INSERT and retrieved with SELECT;
 * @author Nicolas de Pomereu
 *
 */
public class OrderlogRow {

    private int customerId;
    private int itemId;
    private String description;
    private BigDecimal itemCost;
    private Date datePlaced;
    private Timestamp dateShipped;
    private File jpegImage;
    private boolean isDelivered;
    private int quantity;    
    

    /**
     * Create an instance with default values.
     */
    public OrderlogRow() {
	
	customerId = 1;
	itemId = 11;
	description = "customer id 1 and item id 1";
	itemCost = new BigDecimal(2000);
	datePlaced = new Date(System.currentTimeMillis());
	dateShipped = new Timestamp(System.currentTimeMillis());
	jpegImage = new File(ConnectionParms.IN_DIRECTORY + File.separator + "username_koala.jpg");
	isDelivered = true;
	quantity = 3000;
    }


    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }


    /**
     * @return the itemId
     */
    public int getItemId() {
        return itemId;
    }


    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }


    /**
     * @return the itemCost
     */
    public BigDecimal getItemCost() {
        return itemCost;
    }


    /**
     * @return the datePlaced
     */
    public Date getDatePlaced() {
        return datePlaced;
    }


    /**
     * @return the dateShipped
     */
    public Timestamp getDateShipped() {
        return dateShipped;
    }


    /**
     * @return the jpegImage
     */
    public File getJpegImage() {
        return jpegImage;
    }


    /**
     * @return the isDelivered
     */
    public boolean isDelivered() {
        return isDelivered;
    }


    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }


    @Override
    public String toString() {
	return "OrderlogRow [customerId=" + customerId + ", itemId=" + itemId + ", description=" + description
		+ ", itemCost=" + itemCost + ", datePlaced=" + datePlaced + ", dateShipped=" + dateShipped
		+ ", jpegImage=" + jpegImage + ", isDelivered=" + isDelivered + ", quantity=" + quantity + "]";
    }

    
}
