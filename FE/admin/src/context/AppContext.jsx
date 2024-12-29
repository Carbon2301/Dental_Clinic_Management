import { createContext } from "react";


export const AppContext = createContext()

const AppContextProvider = (props) => {

    const currency = import.meta.env.VITE_CURRENCY
    const backendUrl = import.meta.env.VITE_BACKEND_URL


    // Function to format the date eg. ( 20_01_2000 => 20 Jan 2000 )
    const slotDateFormat = (slotDate) => {
        const months = [
            "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        ];
        
        // Chuyển chuỗi ISO 8601 sang đối tượng Date
        const date = new Date(slotDate);
        
        // Lấy ngày, tháng và năm
        const day = date.getUTCDate(); // Ngày
        const month = months[date.getUTCMonth() + 1]; // Tháng
        const year = date.getUTCFullYear(); // Năm
        
        return `${day} ${month} ${year}`;
    };

    const formatTime = (time) => {
        const [hour, minute] = time.split(':');
        const intHour = parseInt(hour, 10);
        const period = intHour >= 12 ? 'PM' : 'AM';
        const formattedHour = intHour % 12 || 12; // Chuyển sang định dạng 12 giờ, 0 => 12
        return `${formattedHour}:${minute} ${period}`;
    };
    
    // Function to calculate the age eg. ( 20_01_2000 => 24 )
    const calculateAge = (dob) => {
        const today = new Date()
        const birthDate = new Date(dob)
        let age = today.getFullYear() - birthDate.getFullYear()
        return age
    }

    const value = {
        backendUrl,
        currency,
        slotDateFormat,
        calculateAge,
        formatTime
    }

    return (
        <AppContext.Provider value={value}>
            {props.children}
        </AppContext.Provider>
    )

}

export default AppContextProvider