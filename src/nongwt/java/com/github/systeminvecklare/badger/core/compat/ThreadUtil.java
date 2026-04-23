package com.github.systeminvecklare.badger.core.compat;

public class ThreadUtil {
	private static final ThreadLocal<IThread> THREADLOCAL = new ThreadLocal<IThread>() {
		@Override
		protected IThread initialValue() {
			return new ThreadImpl(Thread.currentThread());
		}
	};
	
	public static IThread currentThread() {
		return THREADLOCAL.get();
	}
	
	private static class ThreadImpl implements IThread {
		private final Thread wrapped;

		public ThreadImpl(Thread wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((wrapped == null) ? 0 : wrapped.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ThreadImpl other = (ThreadImpl) obj;
			if (wrapped == null) {
				if (other.wrapped != null)
					return false;
			} else if (!wrapped.equals(other.wrapped))
				return false;
			return true;
		}
	}
}
