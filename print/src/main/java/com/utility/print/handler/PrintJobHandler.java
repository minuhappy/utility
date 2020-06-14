package com.utility.print.handler;

import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

public class PrintJobHandler implements PrintJobListener {

	@Override
	public void printDataTransferCompleted(PrintJobEvent pje) {
		System.out.println("printDataTransferCompleted");
	}

	@Override
	public void printJobCompleted(PrintJobEvent pje) {
		System.out.println("printJobCompleted");
	}

	@Override
	public void printJobFailed(PrintJobEvent pje) {
		System.out.println("printJobFailed");
	}

	@Override
	public void printJobCanceled(PrintJobEvent pje) {
		System.out.println("printJobCanceled");
	}

	@Override
	public void printJobNoMoreEvents(PrintJobEvent pje) {
//		String printName = pje.getPrintJob().getPrintService().getName();
		System.out.println("printJobNoMoreEvents");
	}

	@Override
	public void printJobRequiresAttention(PrintJobEvent pje) {
		System.out.println("printJobRequiresAttention");
	}
}