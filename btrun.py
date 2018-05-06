import btscan

def main():
    bdaddr = btscan.get_bdaddr()
    '''rssi_values = btscan.get_rssi_values(bdaddr)

    while rssi in rssi_values:
        print(rssi)
        '''

if __name__=="__main__":
    main()
