package com.liveco.gateway.plc;

// https://infosys.beckhoff.com/english.php?content=../content/1033/tcsample_java/html/ads_returncodes.htm&id=3242844895172038386

public class AdsException extends PlcException{

	private static final long serialVersionUID= 1L;
	
	public AdsException(int err, String errMessage){
		super(errMessage);
	}
	public AdsException(String errMessage){
		super(errMessage);
	}

	public AdsException(int err){
		super(err);
		String errMessage;
		switch(err){
			case 0x1:
				errMessage = "Internal error";
				break;
			case 0x2:
				errMessage = "No Rtime";
				break;
			case 0x3:
				errMessage = "Allocation locked memory error";
				break;				
			case 0x4:
				errMessage = "Insert mailbox error";
				break;				
			case 0x5:
				errMessage = "Wrong receive HMSG";
				break;				
			case 0x6:
				errMessage = "target port not found";
				break;				
			case 0x7:
				errMessage = "target machine not found";
				break;				
			case 0x8:
				errMessage = "Unknown command ID";
				break;				
			case 0x9:
				errMessage = "Bad task ID";
				break;				
			case 0xA:
				errMessage = "No IO";
				break;				
			case 0xB:
				errMessage = "Unknown ADS command";
				break;
			case 0xC:
				errMessage = "Win 32 error";
				break;
			case 0xD:
				errMessage = "Port not connected";
				break;				
			case 0xE:
				errMessage = "Invalid ADS length";
				break;				
			case 0xF:
				errMessage = "Invalid ADS Net ID";
				break;				
			case 0x10:
				errMessage = "Low Installation level";
				break;				
			case 0x11:
				errMessage = "No debug available";
				break;				
			case 0x12:
				errMessage = "Port disabled";
				break;				
			case 0x13:
				errMessage = "Port already connected";
				break;				
			case 0x14:
				errMessage = "ADS Sync Win32 error";
				break;				
			case 0x15:
				errMessage = "ADS Sync Timeout";
				break;				
			case 0x16:
				errMessage = "ADS Sync AMS error";
				break;				
			case 0x17:
				errMessage = "ADS Sync no index map";
				break;
			case 0x18:
				errMessage = "Invalid ADS port";
				break;
			case 0x19:
				errMessage = "No memory";
				break;				
			case 0x1A:
				errMessage = "TCP send error";
				break;				
			case 0x1B:
				errMessage = "Host unreachable";
				break;				
			case 0x1C:
				errMessage = "Invalid AMS fragment";
				break;				
			case 0x700:
				errMessage = "error class <device error>";
				break;				
			case 0x701:
				errMessage = "Service is not supported by server";
				break;				
			case 0x702:
				errMessage = "invalid index group";
				break;				
			case 0x703:
				errMessage = "invalid index offset";
				break;				
			case 0x704:
				errMessage = "reading/writing not permitted";
				break;				
			case 0x705:
				errMessage = "parameter size not correct";
				break;				
			case 0x706:
				errMessage = "invalid parameter value(s)";
				break;				
			case 0x707:
				errMessage = "device is not in a ready state";
				break;				
			case 0x708:
				errMessage = "device is busy";
				break;				
			case 0x709:
				errMessage = "invalid context (must be in Windows)";
				break;				
			case 0x70A:
				errMessage = "out of memory";
				break;	
			case 0x70B:
				errMessage = "invalid parameter value(s)";
				break;				
			case 0x70C:
				errMessage = "not found (files, ...)";
				break;				
			case 0x70D:
				errMessage = "syntax error in command or file";
				break;					
			case 0x70E:
				errMessage = "objects do not match";
				break;				
			case 0x70F:
				errMessage = "object already exists";
				break;
				
				
				
			case 0x710:
				errMessage = "error class <device error>";
				break;				
			case 0x711:
				errMessage = "symbol version invalid";
				break;				
			case 0x712:
				errMessage = "server is in invalid state";
				break;				
			case 0x713:
				errMessage = "AdsTransMode not supported";
				break;				
			case 0x714:
				errMessage = "Notification handle is invalid";
				break;				
			case 0x715:
				errMessage = "Notification client not registered";
				break;				
			case 0x716:
				errMessage = "no more notification handles";
				break;				
			case 0x717:
				errMessage = "size for watch too big";
				break;				
			case 0x718:
				errMessage = "device not initialized";
				break;				
			case 0x719:
				errMessage = "device has a timeout";
				break;				
			case 0x71A:
				errMessage = "query interface failed";
				break;	
			case 0x71B:
				errMessage = "wrong interface required";
				break;				
			case 0x71C:
				errMessage = "class ID is invalid";
				break;				
			case 0x71D:
				errMessage = "object ID is invalid";
				break;					
			case 0x71E:
				errMessage = "request is pending";
				break;				
			case 0x71F:
				errMessage = "request is aborted";
				break;				
				
				
				
				
			case 0x720:
				errMessage = "signal warning";
				break;					
			case 0x721:
				errMessage = "invalid array index";
				break;				
			case 0x722:
				errMessage = "symbol not active";
				break;				
			case 0x723:
				errMessage = "access denied";
				break;					
			case 0x724:
				errMessage = "missing license";
				break;				
			case 0x72c:
				errMessage = "exception occured during system start";
				break;				
			case 0x740:
				errMessage = "Error class <client error>";
				break;					
			case 0x741:
				errMessage = "invalid parameter at service";
				break;				
			case 0x742:
				errMessage = "polling list is empty";
				break;				
			case 0x743:
				errMessage = "var connection already in use";
				break;					
			case 0x744:
				errMessage = "invoke ID in use";
				break;				
			case 0x745:
				errMessage = "timeout elapsed";
				break;				
			case 0x746:
				errMessage = "error in win32 subsystem";
				break;
			case 0x747:
				errMessage = "Invalid client timeout value";
				break;
			case 0x748:
				errMessage = "ads-port not opened";
				break;		
			case 0x750:
				errMessage = "internal error in ads sync";
				break;		
			case 0x751:
				errMessage = "hash table overflow";
				break;
			case 0x752:
				errMessage = "key not found in hash";
				break;		
			case 0x753:
				errMessage = "no more symbols in cache";
				break;		
			case 0x754:
				errMessage = "invalid response received";
				break;		
			case 0x755:
				errMessage = "sync port is locked";
				break;		
		}
	}	
	
}
