package com.ems.PaymentsService.enums;

import com.ems.PaymentsService.utility.constants.ErrorMessages;

public enum DBRecordStatus
{
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String dbStatus;

    DBRecordStatus(String dbStatus)
    {
        this.dbStatus = dbStatus;
    }

    public String getDBStatus()
    {
        return dbStatus;
    }

    public static DBRecordStatus fromString(String dbStatus)
    {
        for (DBRecordStatus status : DBRecordStatus.values())
        {
            if (status.dbStatus.equalsIgnoreCase(dbStatus))
            {
                return status;
            }
        }
        throw new IllegalArgumentException(ErrorMessages.RECORD_NOT_FOUND);
    }
}
