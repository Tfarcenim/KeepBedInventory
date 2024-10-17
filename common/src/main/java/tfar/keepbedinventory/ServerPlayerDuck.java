package tfar.keepbedinventory;

public interface ServerPlayerDuck {
    int getSavedLevels();
    void setSavedLevels(int levels);
    long getLastValidTimestamp();
    void setLastValidTimestamp(long timestamp);

    SavedInventory getSavedInventory();
}
