import bluetooth
import rssiinquiry

def get_bdaddr():
    target_name = "ibeacon"
    target_address = None

    nearby_devices = bluetooth.discover_devices(duration=10, lookup_names=True, flush_cache=True)
    print(len(nearby_devices))

    for bdaddr in nearby_devices:
        if target_name == bluetooth.lookup_name(bdaddr):
            target_address = bdaddr
            break

    if target_address is not None:
        print("Address found: ", target_address)
    else:
        print("Address NOT found")

    return target_address

def get_socket(bdaddr):
    dev_id = bluetooth.hci_get_route(bdaddr)
    try:
        sock = bluetooth.hci_open_dev(dev_id)
    except:
        print("Error accessing device for socket")

    return sock

def get_mode(sock):
    try:
        mode = rssiinquiry.read_inquiry_mode(sock)
    except:
        print("Error reading mode")
    print("Current mode: ", mode)

    return mode

def set_mode(sock, mode):
    if mode != 1:
        print("Writing mode")
        try:
            result = rssiinquiry.write_inquiry_mode(sock, 1)
        except:
            print("Error writing mode")
        if result != 0:
            print("Error setting mode")
        print("result ", result)

#should it be a trend of values, or another function for trend
def get_rssi_values(bdaddr):
    sock = get_socket(bdaddr)
    mode = get_mode(sock)
    set_mode(sock, mode)

    rssi_results = rssiinquiry.device_inquiry_with_with_rssi(sock)
    return rssi_results
