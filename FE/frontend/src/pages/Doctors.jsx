import React, { useContext, useEffect, useState } from 'react';
import { AppContext } from '../context/AppContext';
import { useNavigate, useParams } from 'react-router-dom';

const Doctors = () => {
  const { speciality } = useParams();
  const [filterDoc, setFilterDoc] = useState([]);
  const [showFilter, setShowFilter] = useState(false);
  const navigate = useNavigate();
  const { dentists } = useContext(AppContext);

  const applyFilter = () => {
    if (speciality) {
      setFilterDoc(dentists.filter((doc) => doc.speciality === speciality));
    } else {
      setFilterDoc(dentists);
    }
  };

  useEffect(() => {
    applyFilter();
  }, [dentists, speciality]);

  const handleDoctorClick = (item) => {
    if (item.isWorking) {
      navigate(`/appointment/${item.id}`);
      window.scrollTo(0, 0);
    }
  };

  return (
    <div>
      <p className="text-gray-600">Browse through the doctors specialist.</p>
      <div className="flex flex-col sm:flex-row items-start gap-5 mt-5">
        <button
          onClick={() => setShowFilter(!showFilter)}
          className={`py-1 px-3 border rounded text-sm transition-all sm:hidden ${showFilter ? 'bg-primary text-white' : ''}`}
        >
          Filters
        </button>
        <div className={`flex-col gap-4 text-sm text-gray-600 ${showFilter ? 'flex' : 'hidden sm:flex'}`}>
          {/* Filter Buttons */}
          <p onClick={() => speciality === 'General Dentistry' ? navigate('/doctors') : navigate('/doctors/General Dentistry')} className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${speciality === 'General Dentistry' ? 'bg-[#E2E5FF] text-black ' : ''}`}> General Dentistry</p>
          <p onClick={() => speciality === 'Prosthodontics' ? navigate('/doctors') : navigate('/doctors/Prosthodontics')} className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${speciality === 'Prosthodontics' ? 'bg-[#E2E5FF] text-black ' : ''}`}>Prosthodontics</p>
          <p onClick={() => speciality === 'Orthodontic' ? navigate('/doctors') : navigate('/doctors/Orthodontic')} className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${speciality === 'Orthodontic' ? 'bg-[#E2E5FF] text-black ' : ''}`}>Orthodontic</p>
          <p onClick={() => speciality === 'Pediatric Dentistry' ? navigate('/doctors') : navigate('/doctors/Pediatric Dentistry')} className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${speciality === 'Pediatric Dentistry' ? 'bg-[#E2E5FF] text-black ' : ''}`}>Pediatric Dentistry</p>
          <p onClick={() => speciality === 'Oral Pathology' ? navigate('/doctors') : navigate('/doctors/Oral Pathology')} className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${speciality === 'Oral Pathology' ? 'bg-[#E2E5FF] text-black ' : ''}`}>Oral Pathology</p>
          <p onClick={() => speciality === 'Cosmetic Dentistry' ? navigate('/doctors') : navigate('/doctors/Cosmetic Dentistry')} className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${speciality === 'Cosmetic Dentistry' ? 'bg-[#E2E5FF] text-black ' : ''}`}>Cosmetic Dentistry</p>
          <p onClick={() => speciality === 'Oral Radiology' ? navigate('/doctors') : navigate('/doctors/Oral Radiology')} className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${speciality === 'Oral Radiology' ? 'bg-[#E2E5FF] text-black ' : ''}`}>Oral Radiology</p>
          <p onClick={() => speciality === 'Implantology' ? navigate('/doctors') : navigate('/doctors/Implantology')} className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${speciality === 'Implantology' ? 'bg-[#E2E5FF] text-black ' : ''}`}>Implantology</p>
        </div>
        <div className="w-full grid grid-cols-auto gap-4 gap-y-6">
          {filterDoc.map((item, index) => (
            <div
              onClick={() => handleDoctorClick(item)}
              className={`border border-[#C9D8FF] rounded-xl overflow-hidden cursor-pointer ${item.isWorking ? 'hover:translate-y-[-10px]' : 'cursor-not-allowed opacity-50'} transition-all duration-600`}
              key={index}
            >
              <img className="bg-[#EAEFFF]" src={item.imgUrl} alt="" />
              <div className="p-4">
                <div className={`flex items-center gap-2 text-sm text-center ${item.isWorking ? 'text-green-500' : 'text-gray-500'}`}>
                  <p className={`w-2 h-2 rounded-full ${item.isWorking ? 'bg-green-500' : 'bg-gray-500'}`}></p>
                  <p>{item.isWorking ? 'Available' : 'Not Available'}</p>
                </div>
                <p className="text-[#262626] text-lg font-medium">{item.name}</p>
                <p className="text-[#5C5C5C] text-sm">{item.speciality}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Doctors;
