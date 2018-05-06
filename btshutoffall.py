import bluetooth

def get_bdaddr():
    try:
        nearby_devices = bluetooth.discover_devices(lookup_names = True)
        print(len(nearby_devices))
        
        return nearby_devices
    except:
        print("error")

def get_socket(bdaddr):
    dev_id = bluetooth.hci_get_route(bdaddr)
    try:
        sock = bluetooth.hci_open_dev(dev_id)
    except:
        print("Error accessing device for socket")

    return sock

def main():
    sockets = []
    nearby = get_bdaddr()
    print(len(nearby))
    for addr in nearby:
        sockets.append(get_socket(addr))

    print(len(sockets))

    for sock in sockets:
        bluetooth.stop_advertising(sock)


if __name__=="__main__":
    main()
